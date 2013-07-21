package com.iglooit.core.base.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

/**
 * To be used by entry points and main presenters that are invoked in an IFrame that initially masks the IFrame.
 * The main presenter should fire the event when it is ready to be unmasked.
 * The entry point should listen for it and handle it by calling sendMessageUp(ClarityDataMessageTypes.UNMASK_ME).
 */
public class UnMaskMeEvent extends GwtEvent<UnMaskMeEvent.Handler>
{
    public static final Type<Handler> TYPE = new Type<Handler>();

    public UnMaskMeEvent()
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
        void handle(UnMaskMeEvent closeButtonSelectedEvent);
    }
}

