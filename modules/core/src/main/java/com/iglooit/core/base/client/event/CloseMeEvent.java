package com.iglooit.core.base.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class CloseMeEvent extends GwtEvent<CloseMeEvent.Handler>
{
    public static final Type<Handler> TYPE = new Type<Handler>();

    private String entityId;

    public CloseMeEvent()
    {
    }

    public CloseMeEvent(String entityId)
    {
        this.entityId = entityId;
    }

    public String getEntityId()
    {
        return entityId;
    }

    @Override
    public Type<Handler> getAssociatedType()
    {
        return TYPE;
    }

    @Override
    protected void dispatch(Handler handler)
    {
        handler.handle(this);
    }

    public interface Handler extends EventHandler
    {
        void handle(CloseMeEvent event);
    }
}