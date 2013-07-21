package com.iglooit.core.base.client.event;

import com.google.gwt.event.shared.GwtEvent;

public class AddURLWidgetEvent extends GwtEvent<AddURLWidgetEventHandler>
{
    private static final Type<AddURLWidgetEventHandler> TYPE = new Type<AddURLWidgetEventHandler>();

    private String title;
    private String urlLink;

    public AddURLWidgetEvent()
    {
    }

    public AddURLWidgetEvent(String title, String urlLink)
    {
        this.title = title;
        this.urlLink = urlLink;
    }

    public static Type<AddURLWidgetEventHandler> getType()
    {
        return TYPE;
    }

    @Override
    public Type<AddURLWidgetEventHandler> getAssociatedType()
    {
        return TYPE;
    }

    @Override
    protected void dispatch(AddURLWidgetEventHandler handler)
    {
        handler.handle(this);
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getUrlLink()
    {
        return urlLink;
    }

    public void setUrlLink(String urlLink)
    {
        this.urlLink = urlLink;
    }

}
