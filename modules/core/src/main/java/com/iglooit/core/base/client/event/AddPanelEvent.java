package com.iglooit.core.base.client.event;

import com.clarity.core.base.client.widget.layoutcontainer.GPanel;
import com.google.gwt.event.shared.GwtEvent;

public class AddPanelEvent extends GwtEvent<AddPanelEventHandler>
{
    private final GPanel panel;

    private static final Type<AddPanelEventHandler> TYPE = new Type<AddPanelEventHandler>();

    public AddPanelEvent(GPanel panel)
    {
        this.panel = panel;
    }

    public static Type<AddPanelEventHandler> getType()
    {
        return TYPE;
    }

    @Override
    public Type<AddPanelEventHandler> getAssociatedType()
    {
        return TYPE;
    }

    @Override
    protected void dispatch(AddPanelEventHandler addPanelEventHandler)
    {
        addPanelEventHandler.addPanel(panel);
    }
}
