package com.iglooit.core.base.client.widget.menu;

import com.extjs.gxt.ui.client.widget.menu.MenuBar;
import com.extjs.gxt.ui.client.widget.menu.MenuBarItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;

public class GMenuBar extends MenuBar
{
    public void forceLayout()
    {
        layout(true);
    }

    @Override
    public MenuBarItem findItem(Element elem)
    {
        MenuBarItem menuBarItem = null;
        try
        {
            menuBarItem = super.findItem(elem);
        }
        catch (AssertionError e)
        {
            GWT.log("Error in find menubar item", e);
            throw e;
        }
        return menuBarItem;
    }
}
