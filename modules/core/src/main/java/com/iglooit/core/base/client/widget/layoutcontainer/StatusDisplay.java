package com.iglooit.core.base.client.widget.layoutcontainer;

import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.commons.iface.util.StringUtil;
import com.clarity.core.base.client.composition.display.StatusInfo;
import com.clarity.core.base.client.view.BaseViewConstants;
import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.core.base.client.widget.htmlcontainer.MessageBoxHtml;
import com.extjs.gxt.ui.client.event.FxEvent;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.widget.LayoutContainer;

public class StatusDisplay extends LayoutContainer
{
    protected static final BaseViewConstants BVC = I18NFactoryProvider.get(BaseViewConstants.class);

    private MessageBoxHtml messageBox;
    private Listener<FxEvent> effectStartListener;
    private Listener<FxEvent> effectCompleteListener;

    public static final String STYLE_INFO = "status-info-display info-type-info";
    public static final String STYLE_SUCCESS = "status-info-display info-type-success";
    public static final String STYLE_ERROR = "status-info-display info-type-error";
    public static final String STYLE_WARNING = "status-info-display info-type-warning";

    public StatusDisplay()
    {
        super();
        setStyleName(ClarityStyle.STATUS_MESSAGE_WRAPPER);
    }

    public void updateStatusDescription(String message, MessageBoxHtml.MessageType messageType)
    {
        messageBox = new MessageBoxHtml(message, messageType);
        updateMessageBoxHtml(messageBox);
    }

    public void updateStatusDescription(String status)
    {
        messageBox.setMessage(status);
    }


    public static void popupMessage(String title, String message, MessageBoxHtml.MessageType messageType)
    {
        switch (messageType)
        {
            case SUCCESS:
                StatusInfo.displayWithStylePlusIcon(
                    title, message, STYLE_SUCCESS, Resource.ICONS.tick());
                break;
            case ERROR:
                StatusInfo.displayWithStyle(title, message, STYLE_ERROR);
                break;
            case WARNING:
                StatusInfo.displayWithStyle(title, message, STYLE_WARNING);
                break;
            case INFO:
            default:
                StatusInfo.displayWithStyle(title, message, STYLE_INFO);
        }
    }

    public static void popupStaticMessage(String title, String message, MessageBoxHtml.MessageType messageType)
    {
        StatusInfo.displayStaticDialog(title, message, messageType);
    }

    public void updateMessageBoxHtml(MessageBoxHtml messageBoxHtml)
    {
        clearStatusDescription();

        if (effectStartListener != null)
            messageBox.setEffectStartListener(effectStartListener);
        if (effectCompleteListener != null)
            messageBox.setEffectCompleteListener(effectCompleteListener);

        add(messageBoxHtml);
    }

    public void updateStatusDescription(String mainMessage, String reasonMessage,
                                        MessageBoxHtml.MessageType messageType)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(mainMessage);
        if (StringUtil.isNotEmpty(reasonMessage))
        {
            sb.append("<ul class='error-list'>");
            sb.append("<li>");
            sb.append(reasonMessage);
            sb.append("</li>");
            sb.append("</ul>");
        }
        updateStatusDescription(sb.toString(), messageType);
    }

    public void clearStatusDescription()
    {
        removeAll();
    }

    public void setEffectCompleteListener(Listener<FxEvent> callback)
    {
        this.effectCompleteListener = callback;
    }

    public void setEffectStartListener(Listener<FxEvent> callback)
    {
        this.effectStartListener = callback;
    }
}
 
