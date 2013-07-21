package com.iglooit.core.base.client.widget.htmlcontainer;

import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.HtmlContainer;

public class WidgetPanelTitlebarContainerWrapper
{
    public static final String FSM_SELECTOR_PANEL_TITLEBAR_CONTAINER = "fsm-selector-panel-titlebar-container";
    public static final String FSM_SELECTOR_PANEL_TITLEBAR_LABEL = "fsm-selector-panel-titlebar-label";
    public static final String FSM_SELECTOR_PANEL_TITLEBAR_FIELD = "fsm-selector-panel-titlebar-field";

    private HtmlContainer hc;

    public WidgetPanelTitlebarContainerWrapper()
    {
        hc = new HtmlContainer(
            "<div class='" + FSM_SELECTOR_PANEL_TITLEBAR_CONTAINER + "'>" +
                "<span class='" + FSM_SELECTOR_PANEL_TITLEBAR_LABEL + "'></span>" +
                "&nbsp; <span class='" + FSM_SELECTOR_PANEL_TITLEBAR_FIELD + "'></span>" +
                "</div>"
        );
    }

    public HtmlContainer getHc()
    {
        return hc;
    }

    public void updateLabel(String name)
    {
        Html html = new Html(name);
        html.setTagName("span");
        hc.add(html, "span." + FSM_SELECTOR_PANEL_TITLEBAR_LABEL);
    }

    public void updateField(String name)
    {
        Html html = new Html(name);
        html.setTagName("span");
        hc.add(html, "span." + FSM_SELECTOR_PANEL_TITLEBAR_FIELD);
    }
}

