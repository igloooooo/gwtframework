package com.iglooit.core.base.client.comms;

import com.clarity.core.base.client.ClientUUIDFactory;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONObject;

public class ClarityDataCommunication extends JavaScriptObject
{
    protected ClarityDataCommunication()
    {

    }

    public final native String getMessageType() /*-{
        return this.messageType;
    }-*/;

    public final native void setMessageType(String value) /*-{
        this.messageType = value;
    }-*/;

    public final native String getMessageSource() /*-{
        return this.messageSource;
    }-*/;

    public final native void setMessageSource(String source) /*-{
        this.messageSource = source;
    }-*/;

    public final native String getMessageOriginalSource() /*-{
        return this.messageOriginalSource;
    }-*/;

    public final native void setMessageOriginalSource(String source) /*-{
        this.messageOriginalSource = source;
    }-*/;

    public final native String getMessageDestination() /*-{
        return this.messageDestination;
    }-*/;

    public final native void setMessageDestination(String destination) /*-{
        this.messageDestination = destination;
    }-*/;

    public final native String getMessageData() /*-{
        return this.messageData;
    }-*/;

    public final native void setMessageData(String data) /*-{
        this.messageData = data;
    }-*/;

    public final native String getMessageId() /*-{
        return this.messageId;
    }-*/;

    public final native void setMessageId(String data) /*-{
        this.messageId = data;
    }-*/;


    public static final native ClarityDataCommunication buildInterCommunication(String json) /*-{
        return eval('(' + json + ')');
    }-*/;

    public static ClarityDataCommunication createInterCommunication(
        ClarityDataMessageTypes messageType,
        String messageDestination, String messageData)
    {
        String outboundDestination = messageDestination == null ? "" : messageDestination;
        String outboundData = messageData == null ? "" : messageData;

        ClarityDataCommunication clarityDataCommunication =
            buildInterCommunication("{" +
                "'messageType': '" + messageType + "', " +
                "'messageDestination': '" + outboundDestination + "', " +
                "'messageData': '" + outboundData + "'" +
                "}");
        clarityDataCommunication.setMessageId(ClientUUIDFactory.generateRandom().toString());
        return clarityDataCommunication;
    }

    public static String prepareForDeparture(ClarityDataCommunication communication, String identity)
    {
        communication.setMessageSource(identity);
        return new JSONObject(communication).toString();
    }
}
