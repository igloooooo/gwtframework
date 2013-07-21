package com.iglooit.core.base.client.widget.menuitem;

import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.user.client.Event;

public class ClarityTooltipMenuItem extends MenuItem
{
    private boolean customEnabled;

    /**
     * add style to the disabled widget
     *
     * @param enabled
     */
    @Override
    public void setEnabled(boolean enabled)
    {
        this.customEnabled = enabled;
        if (!enabled)
        {
            this.addStyleName(this.disabledStyle);
            this.setCanActivate(false);
        }
        else
        {
            this.removeStyleName(this.disabledStyle);
            super.setEnabled(enabled);
        }
    }

    @Override
    public void onBrowserEvent(Event event)
    {
        if (event.getTypeInt() == Event.ONMOUSEDOWN || event.getTypeInt() == Event.ONCLICK)
        {
            if (!customEnabled)
            {
                // return directly so that no action would be taken.
                return;
            }
        }
        super.onBrowserEvent(event);
    }

    /**
     * overwrite the onClick by adding our own 'customEnabled' flag
     *
     * @param be
     */
    @Override
    protected void onClick(ComponentEvent be)
    {
        be.stopEvent();
        MenuEvent me = new MenuEvent(parentMenu);
        me.setItem(this);
        me.setEvent(be.getEvent());
        if (customEnabled && !disabled && fireEvent(Events.Select, me))
        {
            handleClick(be);
        }
    }
}
