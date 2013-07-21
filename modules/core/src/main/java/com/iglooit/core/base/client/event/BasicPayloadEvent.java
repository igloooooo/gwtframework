package com.iglooit.core.base.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public abstract class BasicPayloadEvent<PAYLOAD> extends GwtEvent<BasicPayloadEvent.Handler>
{
    private PAYLOAD payload;

    public BasicPayloadEvent(PAYLOAD payload)
    {
        this.payload = payload;
    }

    public PAYLOAD getPayload()
    {
        return payload;
    }

    public void setPayload(PAYLOAD payload)
    {
        this.payload = payload;
    }

    @Override
    protected void dispatch(Handler handler)
    {
        handler.handle(this);
    }

    public interface Handler<E extends BasicPayloadEvent> extends EventHandler
    {
        void handle(E event);
    }
}