package com.iglooit.core.base.client.comms;

import com.clarity.commons.iface.util.StringUtil;
import com.clarity.core.base.client.ClientUUIDFactory;
import com.clarity.core.lib.client.AppInjector;
import com.clarity.core.lib.client.UniversalHandlerManager;
import com.google.gwt.user.client.Window;

import java.util.HashMap;

public class UniversalCommsService
{
    // If I am in an IFrame I will have been assigned a unique id by its creator and it will have put it in a
    // URL query parameter having this name. See INavigator.createFrame(...).
    public static final String QUERY_PARAM_PARTICIPANT_ID = "ucspid";

    private final AppInjector injector = AppInjector.INSTANCE;
    private boolean inIFrame;
    private String participantId;
    private UniversalHandlerManager universalEventBus = injector.getUniversalEventBus();

    // Maps registered participants (ie. message source names) to the message source that told us,
    // which will be either our parent window or one of our child windows. In other words,
    // it tells us the first hop in the tree of windows towards the registered participant.
    private HashMap<String, String> messageRouter;

    private HashMap<String, Boolean> seenMessage;
    private String messageLog = "";

    public UniversalCommsService()
    {
        participantId = Window.Location.getParameter(QUERY_PARAM_PARTICIPANT_ID);

        if (StringUtil.isEmpty(participantId))
        {
            inIFrame = false;
            participantId = ClientUUIDFactory.generateRandom().toString();
        }
        else
        {
            inIFrame = true;
        }

        messageRouter = new HashMap<String, String>();
        seenMessage = new HashMap<String, Boolean>();

        initExternalJS(this);

        //Register ourselves globally
        registerParticipation();
    }

    protected native void initExternalJS(UniversalCommsService ep) /*-{
        $wnd.handleInboundMessage = $entry(function (inboundData)
        {
            ep.@com.clarity.core.base.client.comms.UniversalCommsService::handleInboundMessageConverter(Ljava/lang/String;)(inboundData);
        });
    }-*/;

    private native void sendMessageDown(String data) /*-{
        var iframes = $doc.getElementsByTagName("iframe");

        for (var i = 0; i < iframes.length; i++)
        {
            var iframe;
            if (navigator.userAgent.toLowerCase().indexOf("msie") != -1)
            {
                iframe = iframes[i];
            }
            else
            {
                var frame = iframes[i];
                iframe = frame.contentWindow || frame;
            }

            if (iframe.handleInboundMessage)
            {
                iframe.handleInboundMessage(data);
            }
        }

    }-*/;

    private native void sendMessageUp(String t) /*-{
        if ($wnd.parent)
            $wnd.parent.handleInboundMessage(t);
    }-*/;

    private native boolean isTop() /*-{
        if (!$wnd.parent || $wnd.parent === $wnd)
            return true;
        else
            return false;
    }-*/;

    public void sendMessage(ClarityDataMessageTypes messageType, String messageDestination, String messageData)
    {
        ClarityDataCommunication clarityDataCommunication =
            ClarityDataCommunication.createInterCommunication(
                messageType, messageDestination, messageData);

        sendMessage(clarityDataCommunication);
    }

    public void sendMessageUp(ClarityDataMessageTypes messageType)
    {
        sendMessage(messageType, null, null);
    }

    public void sendMessageUp(ClarityDataMessageTypes messageType, String messageDestination, String messageData)
    {
        ClarityDataCommunication clarityDataCommunication =
            ClarityDataCommunication.createInterCommunication(
                messageType, messageDestination, messageData);

        sendMessageUp(clarityDataCommunication);
    }

    private void sendMessage(ClarityDataCommunication message)
    {
        message.setMessageSource(getParticipantId());
        message.setMessageOriginalSource(getParticipantId());
        String sender = ClarityDataCommunication.prepareForDeparture(message, getParticipantId());

        //todo be smarter about directionality of message dispatch
        if (!isTop())
        {
            sendMessageUp(sender);
        }
        sendMessageDown(sender);
    }

    private void sendMessageUp(ClarityDataCommunication message)
    {
        message.setMessageSource(getParticipantId());
        message.setMessageOriginalSource(getParticipantId());
        String sender = ClarityDataCommunication.prepareForDeparture(message, getParticipantId());

        if (!isTop())
        {
            sendMessageUp(sender);
        }
    }

    public void handleInboundMessageConverter(String s)
    {
        ClarityDataCommunication communication = ClarityDataCommunication.buildInterCommunication(s);

        //If we've already seen this message, swallow it.
        if (seenMessage.containsKey(communication.getMessageId()))
        {
            //Window.alert("Seen this message, swallowing");
            return;
        }

        seenMessage.put(communication.getMessageId(), Boolean.TRUE);

        //If this message is from us, ignore it
        if (getParticipantId().equals(communication.getMessageOriginalSource()))
            return;

        ClarityDataMessageTypes messageType = ClarityDataMessageTypes.valueOf(communication.getMessageType());


        if (messageType != ClarityDataMessageTypes.REGISTER_PARTICIPANT)
        {
            if ((communication.getMessageDestination() == null
                || communication.getMessageDestination().length() == 0)
                || getParticipantId().equals(communication.getMessageDestination()))
            {
                messageLog += s + "\n\n";
                universalEventBus.fireEvent(new UniversalEventBusEvent(communication));
            }

            if ((communication.getMessageDestination() == null
                || !communication.getMessageDestination().equals(getParticipantId()))
                && (!communication.getMessageOriginalSource().equals(getParticipantId())))
            {
                //Rebroadcast...
                communication.setMessageSource(getParticipantId());
                String outbound = ClarityDataCommunication.prepareForDeparture(communication, getParticipantId());

                if (!isTop())
                {
                    sendMessageUp(outbound);
                }
                sendMessageDown(outbound);
            }
        }
        else
        {
            //Don't rebroadcast messages from ourself
//            Window.alert("RP: id: " + getParticipantId() + " -> orig:" + communication.getMessageData());
            if (!(getParticipantId().equals(communication.getMessageOriginalSource())))
            {
                if (!messageRouter.containsKey(communication.getMessageOriginalSource()))
                {
                    messageLog += s + "\n\n";

                    messageRouter.put(communication.getMessageOriginalSource(),
                        communication.getMessageSource());

                    //Rebroadcast this message, but register ourselves as the source for routing purposes
                    communication.setMessageSource(getParticipantId());
                    String outbound = ClarityDataCommunication.prepareForDeparture(communication,
                        getParticipantId());

                    if (!isTop())
                    {
                        sendMessageUp(outbound);
                    }
                    sendMessageDown(outbound);

                    //Because we received a message broadcast from a new module, we need to re-register ourselves, so
                    //that the new module can be aware of us, and get a route.
                    registerParticipation();
                }
            }
        }
    }

    protected void registerParticipation()
    {
        ClarityDataCommunication registration = ClarityDataCommunication.createInterCommunication(
            ClarityDataMessageTypes.REGISTER_PARTICIPANT, "", getParticipantId());

        sendMessage(registration);
    }

    public UniversalHandlerManager getUniversalEventBus()
    {
        return universalEventBus;
    }

    protected boolean isInIFrame()
    {
        return inIFrame;
    }

    public String getParticipantId()
    {
        return participantId;
    }

    public String getMessageLog()
    {
        return messageLog;
    }
}
