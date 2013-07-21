package com.iglooit.core.base.client.comms;

import com.clarity.core.lib.client.AppInjector;
import com.clarity.core.lib.client.UniversalHandlerManager;
import com.google.gwt.core.client.EntryPoint;

/**
 * TODO - review whether this is no longer needed - probably better to inject UniversalCommsService where it's needed.
 */
public abstract class GWTCommsEntryPoint implements EntryPoint
{
    public static final String QUERY_PARAM_PARTICIPANT_ID = UniversalCommsService.QUERY_PARAM_PARTICIPANT_ID;
    public static final String PARAM_ENTITY_TYPE = "entitytype";
    public static final String PARAM_ENTITY_ID = "entityid";

    private final AppInjector injector = AppInjector.INSTANCE;

    private UniversalCommsService universalCommsService = injector.getUniversalCommsService();

    public GWTCommsEntryPoint()
    {
        super();
    }

    public void sendMessage(ClarityDataMessageTypes messageType, String messageDestination, String messageData)
    {
        universalCommsService.sendMessage(messageType, messageDestination, messageData);
    }

    public void sendMessageUp(ClarityDataMessageTypes messageType)
    {
        universalCommsService.sendMessageUp(messageType);
    }

    public void sendMessageUp(ClarityDataMessageTypes messageType, String messageDestination, String messageData)
    {
        universalCommsService.sendMessageUp(messageType, messageDestination, messageData);
    }

    public void sendModuleReady()
    {
        universalCommsService.sendMessage(ClarityDataMessageTypes.MODULE_READY, null, getIdentity());
    }

    public UniversalHandlerManager getUniversalEventBus()
    {
        return universalCommsService.getUniversalEventBus();
    }

    protected boolean isInIFrame()
    {
        return universalCommsService.isInIFrame();
    }

    public String getIdentity()
    {
        return universalCommsService.getParticipantId();
    }
}
