package com.iglooit.core.base.client.widget.htmlcontainer;

import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.google.gwt.user.client.ui.Widget;

public class ButtonDisplayContainerWrapper
{
    public static final String FSM_BUTTON_DISPLAY_CONTAINER = "fsm-button-display-container";

    private HtmlContainer hc;

    public ButtonDisplayContainerWrapper()
    {
        hc = new HtmlContainer(
            "<span class='" + FSM_BUTTON_DISPLAY_CONTAINER + "'></span>"
        );
    }

    public void updateButtonDisplay(Widget widget)
    {
        hc.add(widget, "span." + FSM_BUTTON_DISPLAY_CONTAINER);
    }

    public HtmlContainer getHc()
    {
        return hc;
    }
}
