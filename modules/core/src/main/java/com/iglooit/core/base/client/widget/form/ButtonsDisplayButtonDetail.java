package com.iglooit.core.base.client.widget.form;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

import java.util.List;

public class ButtonsDisplayButtonDetail
{
    private int width;
    private boolean hasCancel;
    private String previousText;
    private AbstractImagePrototype icon;
    private String styleName;
    private List<Listener<? extends BaseEvent>> clickListeners;

    public ButtonsDisplayButtonDetail()
    {
    }

    public ButtonsDisplayButtonDetail(int width, boolean hasCancel, String previousText,
                                      AbstractImagePrototype icon, String styleName,
                                      List<Listener<? extends BaseEvent>> clickListeners)
    {
        this.width = width;
        this.hasCancel = hasCancel;
        this.previousText = previousText;
        this.icon = icon;
        this.styleName = styleName;
        this.clickListeners = clickListeners;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }

    public List<Listener<? extends BaseEvent>> getClickListeners()
    {
        return clickListeners;
    }

    public void setClickListeners(List<Listener<? extends BaseEvent>> clickListeners)
    {
        this.clickListeners = clickListeners;
    }

    public boolean isHasCancel()
    {
        return hasCancel;
    }

    public void setHasCancel(boolean hasCancel)
    {
        this.hasCancel = hasCancel;
    }

    public String getPreviousText()
    {
        return previousText;
    }

    public void setPreviousText(String previousText)
    {
        this.previousText = previousText;
    }

    public AbstractImagePrototype getIcon()
    {
        return icon;
    }

    public void setIcon(AbstractImagePrototype icon)
    {
        this.icon = icon;
    }

    public String getStyleName()
    {
        return styleName;
    }

    public void setStyleName(String styleName)
    {
        this.styleName = styleName;
    }
}
