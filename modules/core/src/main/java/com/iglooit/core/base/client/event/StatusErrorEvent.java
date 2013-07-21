package com.iglooit.core.base.client.event;

import com.clarity.core.base.client.widget.htmlcontainer.ActionLinkHtml;
import com.clarity.core.base.client.widget.htmlcontainer.MessageBoxHtml;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.user.client.ui.AbstractImagePrototype;


public class StatusErrorEvent extends GwtEvent<StatusErrorEvent.Handler>
{
    public static final Type<Handler> TYPE = new Type<Handler>();

    private String message;

    private MessageBoxHtml.MessageType messageType;

    private String title;

    private AbstractImagePrototype icon;

    private ActionLinkHtml link;

    public StatusErrorEvent(String message, MessageBoxHtml.MessageType messageType)
    {
        this.message = message;
        this.messageType = messageType;
    }

    public StatusErrorEvent(String message)
    {
        this(message, MessageBoxHtml.MessageType.ERROR);
    }
    
    public StatusErrorEvent(String title, String message)
    {
        this(title, message, null, null);
    }

    public StatusErrorEvent(String title,
                            String message,
                            AbstractImagePrototype icon,
                            ActionLinkHtml link)
    {
        this.title = title;
        this.icon = icon;
        this.link = link;
        this.message = message;
        this.messageType = MessageBoxHtml.MessageType.ERROR;
    }

    public String getMessage()
    {
        return message;
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
        void handle(StatusErrorEvent event);
    }
}
