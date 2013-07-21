package com.iglooit.core.base.client.widget.common;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;

public class ClarityButtonOption extends ClarityWidgetOption
{
    private String title;
    private SelectionListener<ButtonEvent> selectionListener;

    public ClarityButtonOption(String title, SelectionListener<ButtonEvent> selectionListener)
    {
        this.title = title;
        this.selectionListener = selectionListener;
    }

    public String getTitle()
    {
        return title;
    }

    public SelectionListener<ButtonEvent> getSelectionListener()
    {
        return selectionListener;
    }

    @Override
    public WidgetType getWidgetClassString()
    {
        return WidgetType.BUTTON;
    }

    @Override
    public String getDisplayTitle()
    {
        return title;
    }
}
