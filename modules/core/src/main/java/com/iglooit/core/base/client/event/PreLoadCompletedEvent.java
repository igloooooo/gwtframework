package com.iglooit.core.base.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * Created by IntelliJ IDEA. User: ftsang Date: 20/03/13 11:24 AM
 */
public class PreLoadCompletedEvent extends GwtEvent<PreLoadCompletedEvent.Handler>
{
    public static final Type<Handler> TYPE = new Type<Handler>();

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
        void handle(PreLoadCompletedEvent event);
    }
}
