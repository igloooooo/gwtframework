package com.iglooit.core.base.client.widget.common;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.Listener;

/**
 * For any new type of widget option, please also update:
 * - ClarityWidgetHelper.java to make sure widget can get in use
 */
public abstract class ClarityWidgetOption
{
    /* this should only be used internally by grid */
    private Listener<BaseEvent> preClickBaseListener;

    public abstract WidgetType getWidgetClassString();

    public abstract String getDisplayTitle();

    public Listener<BaseEvent> getPreClickBaseListener()
    {
        return preClickBaseListener;
    }

    public void setPreClickBaseListener(Listener<BaseEvent> preClickBaseListener)
    {
        this.preClickBaseListener = preClickBaseListener;
    }

    /**
     * Please remember to update ClarityWidgetHelper.java for any new widget type
     */
    public enum WidgetType
    {
        BUTTON,
        INTEGER_NUMBER_FIELD
    }
}
