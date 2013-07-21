package com.iglooit.core.base.client.widget.layoutcontainer;

import com.clarity.core.base.client.mvp.Display;
import com.extjs.gxt.ui.client.widget.Layout;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractDisplayLayoutContainer extends LayoutContainer implements Display
{
    protected AbstractDisplayLayoutContainer()
    {
    }

    protected AbstractDisplayLayoutContainer(Layout layout)
    {
        super(layout);
    }

    @Override
    public Widget asWidget()
    {
        return this;
    }

    /**
     * This method should not be required for Buttons
     * where the presenter adding the listener and the presenter implements DefaultPresenter
     * as it is the responsibility of the presenter to remove the listeners it added (done on Unbind)
     *
     * ClarityButton adds handler registrations to the DefaultPresenter when a clickHandler is added, which are removed
     * automatically on unbind
     *
     * ClarityComboBox simply ignores the issue of removing listeners - so this method is required for them
     */
    public abstract void removeAllWidgetListeners();
}
