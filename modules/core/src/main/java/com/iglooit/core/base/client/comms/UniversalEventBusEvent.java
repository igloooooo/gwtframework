package com.iglooit.core.base.client.comms;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class UniversalEventBusEvent extends GwtEvent<UniversalEventBusEvent.Handler>
{
    public static final Type<Handler> TYPE = new Type<Handler>();
    private ClarityDataCommunication data;

    public UniversalEventBusEvent(ClarityDataCommunication data)
    {
        this.data = data;
    }

    @Override
    public Type<Handler> getAssociatedType()
    {
        return TYPE;
    }



    public ClarityDataCommunication getData()
    {
        return data;
    }

    @Override
    protected void dispatch(Handler handler)
    {
        handler.handle(this);
    }

    public interface Handler extends EventHandler
    {
        void handle(UniversalEventBusEvent event);
    }
}
