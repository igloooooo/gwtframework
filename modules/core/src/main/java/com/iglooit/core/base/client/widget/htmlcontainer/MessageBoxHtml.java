package com.iglooit.core.base.client.widget.htmlcontainer;

import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.view.FadeHighlightEffect;
import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.commons.iface.type.AppX;
import com.extjs.gxt.ui.client.event.FxEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.fx.FxConfig;
import com.extjs.gxt.ui.client.widget.Html;
import com.extjs.gxt.ui.client.widget.HtmlContainer;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Widget;

public class MessageBoxHtml extends HtmlContainer
{
    private static final String MESSAGE_TEMPLATE =
            "<span class='icon-wrapper'></span>" +
                    "<span class='msg-wrapper'></span></span>" +
                    "<span class='link-wrapper'></span>";

    private static final String ICON_SELECTOR = ".icon-wrapper";
    private static final String MESSAGE_SELECTOR = ".msg-wrapper";
    private static final String LINK_SELECTOR = ".link-wrapper";

    private MessageType messageType;

    private boolean fadeHighlight = false;
    private boolean autoClose = false;
    private FxConfig fxConfig = FxConfig.NONE;

    public MessageBoxHtml()
    {
        super(MESSAGE_TEMPLATE);
    }

    public MessageBoxHtml(MessageType type)
    {
        this();
        setDefaultStyling(type);
    }

    public MessageBoxHtml(String message, MessageType type)
    {
        this(type);
        setMessage(message);
    }

    public MessageBoxHtml(String message, MessageType type, AbstractImagePrototype icon)
    {
        this(type);
        setMessage(message);
        setIcon(icon);
    }

    public void setMessage(String message)
    {
        Html messageHtml = new Html(message);
        messageHtml.setTagName("span");
        add(messageHtml, MESSAGE_SELECTOR);
    }

    // set a widget as the message (as opposed to a String)
    // This brings more flexibility as we can now use a pre built Html or HtmlContainer as the message
    public void setMessage(Widget widget)
    {
        add(widget, MESSAGE_SELECTOR);
    }

    public void setIcon(AbstractImagePrototype icon)
    {
        if (icon == null)
        {
            Html iconHtml = new Html("");
            add(iconHtml, ICON_SELECTOR);
        }
        else
        {
            Html iconHtml = new Html(icon.getHTML());
            iconHtml.setTagName("span");
            add(iconHtml, ICON_SELECTOR);
        }
    }

    public void setActionLink(ActionLinkHtml link)
    {
        link.setTagName("span");
        add(link, LINK_SELECTOR);
    }

    public void setActionLink(ActionLinkHtml link, boolean onNewLine)
    {
        if (onNewLine)
            link.setStyleAttribute("display", "block");
        else
            link.setTagName("span");
        add(link, LINK_SELECTOR);
    }

    public void setType(MessageType type)
    {
        this.messageType = type;
        setStyleName(type.value());
    }

    public void enableFadeHighlight(boolean fade)
    {
        this.fadeHighlight = fade;
    }

    public void enableAutoClose(boolean autoClose)
    {
        this.autoClose = autoClose;
    }

    public void setEffectCompleteListener(Listener<FxEvent> callback)
    {
        fxConfig.setEffectCompleteListener(callback);
    }

    public void setEffectStartListener(Listener<FxEvent> callback)
    {
        fxConfig.setEffectStartListener(callback);
    }

    @Override
    protected void onRender(Element target, int index)
    {
        super.onRender(target, index);

        if (fadeHighlight)
        {
            final String startBackgroundColor;
            final String startBorderColor;
            final String endBackgroundColor = "F8F8F8";
            final String endBorderColor = "DDDDDD";
            final int duration = 1500;

            switch (messageType)
            {
                case SUCCESS:
                    startBackgroundColor = ClarityStyle.MESSAGE_BOX_SUCCESS_BACKGROUND;
                    startBorderColor = ClarityStyle.MESSAGE_BOX_SUCCESS_BORDER;
                    break;
                case INFO:
                    startBackgroundColor = ClarityStyle.MESSAGE_BOX_INFO_BACKGROUND;
                    startBorderColor = ClarityStyle.MESSAGE_BOX_INFO_BORDER;
                    break;
                case INFO_ALT:
                    startBackgroundColor = ClarityStyle.MESSAGE_BOX_INFO_ALT_BACKGROUND;
                    startBorderColor = ClarityStyle.MESSAGE_BOX_INFO_ALT_BORDER;
                    break;
                case ERROR:
                case ERROR_STRONG:
                    startBackgroundColor = ClarityStyle.MESSAGE_BOX_ERROR_BACKGROUND;
                    startBorderColor = ClarityStyle.MESSAGE_BOX_ERROR_BORDER;
                    break;
                case STRONGINFO:
                case WARNING:
                    startBackgroundColor = ClarityStyle.MESSAGE_BOX_WARNING_BACKGROUND;
                    startBorderColor = ClarityStyle.MESSAGE_BOX_WARNING_BORDER;
                    break;
                default:
                    startBackgroundColor = endBackgroundColor;
                    startBorderColor = endBorderColor;
                    break;
            }

            // delay the triggering of the fade effect
            Timer t = new Timer()
            {
                @Override
                public void run()
                {
                    FadeHighlightEffect.highlight(el(), "background-color",
                            startBackgroundColor, endBackgroundColor, duration);
                    FadeHighlightEffect.highlight(el(), "border-color",
                            startBorderColor, endBorderColor, duration);
                    if (autoClose)
                        hideMessage();
                }
            };
            t.schedule(3000);
        }

        if (autoClose && !fadeHighlight)
        {
            hideMessage();
        }
    }

    private void hideMessage()
    {
        Timer t = new Timer()
        {
            @Override
            public void run()
            {
                el().fadeToggle(fxConfig);
            }
        };
        t.schedule(4000);
    }

    public void setDefaultStyling(MessageType messageType)
    {
        setType(messageType);
        setIcon(getDefaultIcon(messageType));
        switch (messageType)
        {
            case SUCCESS:
                enableFadeHighlight(true);
                enableAutoClose(true);
                break;
            default:
                break;
        }
    }

    public static AbstractImagePrototype getDefaultIcon(MessageType messageType)
    {
        switch (messageType)
        {
            case ERROR:
            case ERROR_STRONG:
                return Resource.ICONS.exclamationRed();
            case STRONGINFO:
            case WARNING:
                return Resource.ICONS.exclamationYellow();
            case SUCCESS:
                return Resource.ICONS.tick();
            case INFO:
            case INFO_ALT:
                return Resource.ICONS.information();
            default:
                throw new AppX("Unhandled message type");
        }
    }


    public static enum MessageType
    {
        ERROR(ClarityStyle.MESSAGE_BOX_ERROR),
        ERROR_STRONG(ClarityStyle.MESSAGE_BOX_ERROR),  // use modal message box to handle this
        SUCCESS(ClarityStyle.MESSAGE_BOX_SUCCESS),
        SUCCESS_GLOBAL(ClarityStyle.MESSAGE_BOX_SUCCESS),
        INFO(ClarityStyle.MESSAGE_BOX_INFO),
        INFO_ALT(ClarityStyle.MESSAGE_BOX_INFO_ALT),
        STRONGINFO(ClarityStyle.MESSAGE_BOX_WARNING),
        WARNING(ClarityStyle.MESSAGE_BOX_WARNING);

        private final String value;

        private MessageType(String value)
        {
            this.value = value;
        }

        public String value()
        {
            return value;
        }
    }
}
