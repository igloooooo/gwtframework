package com.iglooit.core.base.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class CheckMandatoryFieldsEvent extends GwtEvent<CheckMandatoryFieldsEvent.Handler>
{
    public static final Type<Handler> TYPE = new Type<Handler>();

    public CheckMandatoryFieldsEvent()
    {
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
        void handle(CheckMandatoryFieldsEvent event);
    }
}
