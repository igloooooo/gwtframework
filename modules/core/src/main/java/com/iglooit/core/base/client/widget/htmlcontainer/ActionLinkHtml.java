package com.iglooit.core.base.client.widget.htmlcontainer;

import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.ui.AbstractImagePrototype;

public class ActionLinkHtml extends HtmlContainer
{
    private Html icon;
    private Html link;


    private static final String CONTAINER_STYLE = "action-link-container";
    private static final String CONTAINER_TAG = "div";
    private static final String INNER_TAG_NAME = "span";
    private static final String LINK_STYLE = "link";
    private static final String ICON_STYLE = "icon-wrapper";

    private static final String CONTAINER_HTML =
        "<" + CONTAINER_TAG + " class=" + CONTAINER_STYLE + ">" +
            "<span class=" + ICON_STYLE + "></span>" +
            "<span class=" + LINK_STYLE + "></span></" + CONTAINER_TAG + ">";

    @Override
    public void sinkEvents(int eventBitsToAdd)
    {
        this.link.sinkEvents(eventBitsToAdd);
    }

    @Override
    public void addListener(EventType eventType, Listener<? extends BaseEvent> listener)
    {
        this.link.addListener(eventType, listener);
    }

    public ActionLinkHtml()
    {
        super(CONTAINER_HTML);
    }

    public ActionLinkHtml(String linkName)
    {
        this();
        initLink(linkName);
    }

    public ActionLinkHtml(String linkName, AbstractImagePrototype icon)
    {
        this();
        initIcon(icon);
        initLink(linkName);
    }

    public void setLinkName(String linkName)
    {
        link.setHtml(linkName);
    }

    public void setLinkName(String linkName, AbstractImagePrototype icon)
    {
        link.setHtml(linkName);
        initIcon(icon);
    }

    private void initLink(String linkName)                
    {
        this.link = new Html(linkName);
        this.link.setTagName(INNER_TAG_NAME);
        this.link.setLayoutData(new FitLayout());
        add(this.link, "." + LINK_STYLE);
    }

    private void initIcon(AbstractImagePrototype icon)
    {
        this.icon = new Html(icon.getHTML());
        this.icon.setTagName(INNER_TAG_NAME);
        add(this.icon, "." + ICON_STYLE);
    }


    public void replaceLinkName(String newLinkName)
    {
        this.link.setHtml(newLinkName);
    }
}
