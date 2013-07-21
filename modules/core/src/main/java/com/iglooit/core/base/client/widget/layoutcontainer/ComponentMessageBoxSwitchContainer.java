package com.iglooit.core.base.client.widget.layoutcontainer;

import com.clarity.core.base.client.widget.htmlcontainer.MessageBoxHtml;
import com.extjs.gxt.ui.client.widget.Component;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;

/**
 * Convenience method that allows you to switch between displaying a message box (e.g. info message, warning message,
 * error message, etc.) and a component/widget. This is ideal when you have an empty container and you'd like to
 * display some kind of message box before the user has done something to trigger the display of a component/widget. 
 */
public class ComponentMessageBoxSwitchContainer extends LayoutContainer
{
    private Component component;
    private MessageBoxHtml messageBox;
    private RowData messageBoxRowData;
    private String componentStyle;
    private static final String MESSAGE_BOX_PANEL_STYLE = "message-box-panel";

    public ComponentMessageBoxSwitchContainer(Component component, MessageBoxHtml messageBox, RowData messageBoxRowData)
    {
        this.messageBoxRowData = messageBoxRowData;
        this.component = component;
        this.messageBox = messageBox;
        this.componentStyle = "";
        setComponentVisible(false);
    }

    public ComponentMessageBoxSwitchContainer(Component component, MessageBoxHtml messageBox)
    {
        this(component, messageBox, new RowData(-1, -1));
    }

    /**
     * Convenience method that creates an info message box by default
     * @param component
     * @param message
     */
    public ComponentMessageBoxSwitchContainer(Component component, String message)
    {
        this(component, new MessageBoxHtml(message, MessageBoxHtml.MessageType.INFO), new RowData(-1, -1));
    }

    public void setComponentVisible(boolean visible)
    {
        if (getItemCount() > 0 && (getItem(0) instanceof MessageBoxHtml == !visible)) return;

        removeAll();
        if (visible)
        {
            setLayout(new FitLayout());
            if (!componentStyle.isEmpty())
                addStyleName(componentStyle);
            add(component);
            removeStyleName(MESSAGE_BOX_PANEL_STYLE);
        }
        else
        {
            setLayout(new RowLayout());
            if (!componentStyle.isEmpty())
                removeStyleName(componentStyle);
            add(messageBox, messageBoxRowData);
            addStyleName(MESSAGE_BOX_PANEL_STYLE);
        }
        layout(true);
    }

    public void setComponentStyle(String style)
    {
        componentStyle = style;
    }
}
