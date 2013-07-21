package com.iglooit.core.base.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class RequestCompleteEvent extends GwtEvent<RequestCompleteEvent.Handler>
{
    public static final Type<Handler> TYPE = new Type<Handler>();

    private boolean result;
    private String message;

    public RequestCompleteEvent(boolean result, String message)
    {
        this.message = message;
        this.result = result;
    }

    public String getMessage()
    {

        return message;
    }

    public boolean getResult()
    {
        return result;
    }

    @Override
    public Type<Handler> getAssociatedType()
    {
        return TYPE;
    }

    @Override
    protected void dispatch(Handler requestCompleteEvent)
    {
        requestCompleteEvent.handle(this);
    }

    public interface Handler extends EventHandler
    {
        void handle(RequestCompleteEvent event);
    }
}
