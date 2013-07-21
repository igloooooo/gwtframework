package com.iglooit.core.base.client.event;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.Widget;

public class ClosePanelEvent extends GwtEvent<ClosePanelEventHandler>
{
    private static final Type<ClosePanelEventHandler> TYPE = new Type<ClosePanelEventHandler>();
    private final Widget widget;

    public ClosePanelEvent(Widget widget)
    {
        this.widget = widget;
    }

    public static Type<ClosePanelEventHandler> getType()
    {
        return TYPE;
    }

    @Override
    public Type<ClosePanelEventHandler> getAssociatedType()
    {
        return TYPE;
    }

    @Override
    protected void dispatch(ClosePanelEventHandler closePanelEventHandler)
    {
        closePanelEventHandler.closePanel(widget);
    }
}
