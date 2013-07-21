package com.iglooit.core.base.client.presenter;

import com.clarity.core.base.client.mvp.DefaultPresenter;
import com.clarity.core.base.client.mvp.Display;
import com.extjs.gxt.ui.client.widget.form.Field;
import com.google.gwt.event.shared.HandlerManager;

public abstract class CustomComponentPresenter<D extends Display> extends DefaultPresenter<D>
{
    private boolean allowBlank;

    public CustomComponentPresenter(D display)
    {
        super(display);
    }

    public CustomComponentPresenter(D display, HandlerManager sharedEventBus)
    {
        super(display, sharedEventBus);
    }

    public void setAllowBlank(boolean allowBlank)
    {
        this.allowBlank = allowBlank;
        if (!allowBlank)
        {
            makeFieldRequired();
        }
    }

    public boolean isAllowBlank()
    {
        return allowBlank;
    }

    public abstract Field getField();
    public abstract String getValue();
    public abstract void setValue(String value);
    protected abstract void makeFieldRequired();
}
