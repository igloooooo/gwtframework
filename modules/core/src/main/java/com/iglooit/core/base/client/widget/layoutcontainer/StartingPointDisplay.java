package com.iglooit.core.base.client.widget.layoutcontainer;

import com.clarity.core.base.client.mvp.Display;
import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.widget.htmlcontainer.MessageBoxHtml;
import com.clarity.core.base.client.widget.htmlcontainer.MessageBoxHtml.MessageType;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Widget;

public class StartingPointDisplay extends GPanel implements Display
{
    private MessageBoxHtml messageBox;
    private boolean plain = false;
    private LayoutContainer panel;

    private Widget currentWidget;
    private Widget lastWidget;

    public StartingPointDisplay()
    {
        setLayout(new FitLayout());
    }

    /**
     * Convenience method that creates a MessageBoxHtml widget and sets it's style and message to what you specify
     */
    public void setDefaultMessage(String message, MessageBoxHtml.MessageType messageType)
    {
        setDefaultMessage(message, messageType, new Margins(5));
    }

    public void setDefaultMessage(String message, MessageType messageType, Margins messageMargin)
    {
        Margins margins = messageMargin;
        if (margins == null)
            margins = new Margins(5);
        if (plain)
        {
            panel = new LayoutContainer(new RowLayout(Style.Orientation.HORIZONTAL));
        }
        else
        {
            panel = new ContentPanel();
            panel.addStyleName(ClarityStyle.ROUNDED_CORNERS);
            panel.setLayout(new RowLayout(Style.Orientation.HORIZONTAL));
            ((ContentPanel)panel).setHeaderVisible(false);
        }

        messageBox = new MessageBoxHtml(messageType);
        messageBox.setMessage(message);
        panel.add(messageBox, new RowData(-1, -1, margins));
        panel.addStyleName("starting-point-panel");
        removeAll();
        add(panel);
    }

    public void setMessageIcon(AbstractImagePrototype icon)
    {
        messageBox.setIcon(icon);
    }

    public void revertToStartingPoint()
    {
        setContent(panel);
    }

    public void setPanelBorderVisible(boolean visible)
    {
        panel.setBorders(visible);
        if (panel instanceof ContentPanel)
        {
            ((ContentPanel)panel).setBodyBorder(false);
        }
    }

    /**
     * Allows you to set the content of this display to whatever you want.
     */
    public void setContent(Widget widget)
    {
        lastWidget = currentWidget;
        removeAll();
        add(widget);
        currentWidget = widget;
    }

    public Widget getCurrentWidget()
    {
        return currentWidget;
    }

    public Widget getLastWidget()
    {
        return lastWidget;
    }

    public MessageBoxHtml getMessageBox()
    {
        return messageBox;
    }

    public void setPlain(boolean plain)
    {
        this.plain = plain;
    }

    @Override
    public Widget asWidget()
    {
        return this;
    }

    @Override
    public void doOnRender(Element element, int i)
    {
        layout();
    }

    @Override
    public String getLabel()
    {
        return "";
    }
}
