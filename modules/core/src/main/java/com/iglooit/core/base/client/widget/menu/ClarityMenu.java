package com.iglooit.core.base.client.widget.menu;

import com.extjs.gxt.ui.client.event.PreviewEvent;
import com.extjs.gxt.ui.client.util.BaseEventPreview;
import com.extjs.gxt.ui.client.widget.menu.Menu;

//This class is to fix GXT version 2.2.4 bug
//once we upgrade to GXT higher version, this class should be removed

//http://www.sencha.com/forum/showthread.php?134690
//http://www.sencha.com/forum/showthread.php?133170-gridfilters-can-
// show-more-than-one-context-menu-when-you-have-non-filterable-cols-mix

public class ClarityMenu extends Menu
{
    public ClarityMenu()
    {
        super();
        eventPreview = new BaseEventPreview()
        {
            @Override
            protected boolean onPreview(PreviewEvent pe)
            {
                ClarityMenu.this.onAutoHide(pe);
                return super.onPreview(pe);
            }

            @Override
            protected void onPreviewKeyPress(PreviewEvent pe)
            {
                super.onPreviewKeyPress(pe);
                onEscape(pe);
            }
        };

        //this is the only difference between 2.2.4 and 2.2.5
        eventPreview.setAutoHide(false);
    }
}
