package com.iglooit.core.base.client.event;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

public class UpdateSizeIFrameEvent extends GwtEvent<UpdateSizeIFrameEvent.Handler>
{
    public static final Type<Handler> TYPE = new Type<Handler>();

    private Integer height;

    public UpdateSizeIFrameEvent()
    {
    }

    public UpdateSizeIFrameEvent(Integer height)
    {
        this.height = height;
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
        void handle(UpdateSizeIFrameEvent closeButtonSelectedEvent);
    }

    public Integer getHeight()
    {
        return height;
    }

    public void setHeight(Integer height)
    {
        this.height = height;
    }
}
