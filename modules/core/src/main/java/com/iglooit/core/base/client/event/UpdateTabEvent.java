package com.iglooit.core.base.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class UpdateTabEvent extends GwtEvent<UpdateTabEventHandler>
{
    private String tabId;

    private static final Type<UpdateTabEventHandler> TYPE = new Type<UpdateTabEventHandler>();

    public UpdateTabEvent(String tabId)
    {
        this.tabId = tabId;
    }

    public String getTabId()
    {
        return tabId;
    }

    public static Type<UpdateTabEventHandler> getType()
    {
        return TYPE;
    }

    @Override
    public Type<UpdateTabEventHandler> getAssociatedType()
    {
        return TYPE;
    }

    @Override
    protected void dispatch(UpdateTabEventHandler handler)
    {
        handler.updateTabDisplay(this);
    }
}
