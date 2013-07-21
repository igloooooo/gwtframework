package com.iglooit.core.base.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class MasterViewItemNullSelectionEvent extends GwtEvent<MasterViewItemNullSelectionEvent.Handler>
{

    public static final Type<Handler> TYPE = new Type<Handler>();

    public MasterViewItemNullSelectionEvent()
    {

    }

    @Override
    protected void dispatch(Handler handler)
    {
        handler.handle(this);
    }

    @Override
    public Type<Handler> getAssociatedType()
    {
        return TYPE;
    }

    public interface Handler extends EventHandler
    {
        void handle(MasterViewItemNullSelectionEvent event);
    }
}
