package com.iglooit.core.base.client.event;

import com.clarity.core.base.client.widget.htmlcontainer.ActionLinkHtml;
import com.clarity.core.base.client.widget.htmlcontainer.MessageBoxHtml;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.AbstractImagePrototype;


public class StatusInfoEvent extends GwtEvent<StatusInfoEvent.Handler>
{
    public static final Type<Handler> TYPE = new Type<Handler>();

    private String statusMessage;

    private MessageBoxHtml.MessageType messageType;

    private String title;

    private AbstractImagePrototype icon;

    private ActionLinkHtml link;

    public StatusInfoEvent(String statusMessage)
    {
        this.statusMessage = statusMessage;
        messageType = MessageBoxHtml.MessageType.INFO;
    }

    public StatusInfoEvent(String statusMessage, MessageBoxHtml.MessageType messageType)
    {
        this.statusMessage = statusMessage;
        this.messageType = messageType;
    }

    public StatusInfoEvent(String title,
                              String message,
                              AbstractImagePrototype icon,
                              ActionLinkHtml link)
    {
        this.title = title;
        this.icon = icon;
        this.link = link;
        this.statusMessage = message;
        this.messageType = MessageBoxHtml.MessageType.INFO;
    }

    public String getMessage()
    {
        return statusMessage;
    }

    public MessageBoxHtml.MessageType getMessageType()
    {
        return messageType;
    }

    public String getTitle()
    {
        return title;
    }

    public AbstractImagePrototype getIcon()
    {
        return icon;
    }

    public ActionLinkHtml getLink()
    {
        return link;
    }

    @Override
    public Type<Handler> getAssociatedType()
    {
        return TYPE;
    }

    @Override
    protected void dispatch(Handler handler)
    {
        handler.handle(this);
    }

    public interface Handler extends EventHandler
    {
        void handle(StatusInfoEvent event);
    }
}
