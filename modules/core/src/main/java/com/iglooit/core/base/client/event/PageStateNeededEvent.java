package com.iglooit.core.base.client.event;

import com.clarity.core.base.client.model.IExecuteWithPageState;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;


public class PageStateNeededEvent extends GwtEvent<PageStateNeededEvent.Handler>
{
    public static final Type<Handler> TYPE = new Type<Handler>();

    private IExecuteWithPageState pageStateNeededBy;

    public PageStateNeededEvent(IExecuteWithPageState pageStateNeededBy)
    {
        this.pageStateNeededBy = pageStateNeededBy;
    }

    public IExecuteWithPageState getPageStateNeededBy()
    {
        return pageStateNeededBy;
    }

    @Override
    public Type<Handler> getAssociatedType()
    {
        return TYPE;
    }

    @Override
    protected void dispatch(Handler eventHandler)
    {
        eventHandler.handle(this);
    }


    public interface Handler extends EventHandler
    {
        void handle(PageStateNeededEvent event);
    }
}

