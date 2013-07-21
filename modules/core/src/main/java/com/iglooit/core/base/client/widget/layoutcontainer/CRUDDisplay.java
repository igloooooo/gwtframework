package com.iglooit.core.base.client.widget.layoutcontainer;

import com.clarity.core.base.client.mvp.Display;
import com.clarity.core.base.client.widget.form.ButtonsDisplay;
import com.clarity.core.base.client.widget.htmlcontainer.MessageBoxHtml;
import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.type.NonSerOpt;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.ContentPanel;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.extjs.gxt.ui.client.widget.layout.MarginData;
import com.extjs.gxt.ui.client.widget.layout.RowData;
import com.extjs.gxt.ui.client.widget.layout.RowLayout;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Widget;

/**
 * Standard form container that displays and layouts the following form components:
 * - Status Display (Top): Container that shows different form related messages such as errors, success, info, etc
 * - Form (Middle): The form itself via a FormDisplay
 * - Button Display (Bottom): Container that holds the different form buttons e.g. OK , Cancel
 */

public abstract class CRUDDisplay extends GPanel implements Display
{
    private boolean initCalled = false;

    private ContentPanel panel;
    private LayoutContainer formContainer;
    private StatusDisplay formStatusDisplay = new StatusDisplay();
    private ButtonsDisplay buttonsDisplay;
    private FormDisplay formDisplay;
    private Margins margins = new Margins(5);

    private boolean wrapInPanel = false;

    private LayoutContainer buttonDisplayContainer;

    /**
     * Allows you to specify whether you want this form container wrapped in a content panel (good for standalone forms)
     * or if you want it plain (good when nesting inside other containers such as tabs or when it is one of many
     * components on a page).
     */
    protected CRUDDisplay(boolean wrapInPanel)
    {
        this.wrapInPanel = wrapInPanel;

        // create layout container for each of the form elements
        formContainer = new LayoutContainer();
        formContainer.setLayout(new RowLayout(Style.Orientation.VERTICAL));

        buttonDisplayContainer = new LayoutContainer();
        buttonDisplayContainer.setLayout(new RowLayout(Style.Orientation.VERTICAL));

        if (wrapInPanel)
        {
            panel = new ContentPanel();
            panel.setFrame(true);
            panel.setHeading("...");
            panel.setLayout(new FitLayout());
            panel.setStyleName("crud-panel");
            panel.setScrollMode(Style.Scroll.AUTOY);

            formContainer.setAutoHeight(true);
            formContainer.setAutoWidth(true);

            setLayout(new FitLayout());
            add(panel);
        }
        else
        {
            setLayout(new RowLayout(Style.Orientation.VERTICAL));
            setAutoHeight(true);
        }
    }

    protected CRUDDisplay()
    {
        this(false);
    }

    public void init()
    {
        initCalled = true;
        formContainer.add(formStatusDisplay, new RowData(1, -1, new Margins(0, 0, 5, 0)));
        formContainer.add(formDisplay.asWidget(), new RowData(1, -1, new Margins(0)));
        formContainer.add(buttonDisplayContainer, new RowData(1, -1, new Margins(5, 0, 0, 0)));

        if (buttonsDisplay != null)
        {
            updateButtonDisplayContainer(buttonsDisplay.asWidget());
        }
        if (wrapInPanel)
        {
            panel.add(formContainer, new MarginData(margins));
        }
        else
        {
            add(formContainer, new RowData(1, -1, margins));
        }
    }

    public void reInitFormContainer()
    {
        formContainer.removeAll();
        formContainer.add(formStatusDisplay, new RowData(1, -1, new Margins(0, 0, 5, 0)));
        formContainer.add(formDisplay.asWidget(), new RowData(1, -1, new Margins(0)));
        formContainer.add(buttonDisplayContainer, new RowData(1, -1, new Margins(5, 0, 0, 0)));
    }

    public void updateButtonDisplayContainer(Widget widget)
    {
        buttonDisplayContainer.removeAll();
        buttonDisplayContainer.add(widget, new RowData(1, -1));
        layout(true);
    }


    /**
     * Set margins on overall form container
     *
     * @param margins
     */
    public void setMargins(Margins margins)
    {
        this.margins = margins;
    }

    public Widget asWidget()
    {
        return this;
    }

    public void doOnRender(Element element, int i)
    {
        if (!initCalled)
            throw new AppX("init must be called");
        layout();
    }

    public void updateStatusDescription(String status, MessageBoxHtml.MessageType messageType)
    {
        formStatusDisplay.updateStatusDescription(status, messageType);
        layout(true);
    }

    public void updateStatusDescription(String status, String reason,
                                        MessageBoxHtml.MessageType messageType)
    {
        formStatusDisplay.updateStatusDescription(status, reason, messageType);
        layout(true);
    }

    public void clearStatusDescription()
    {
        formStatusDisplay.clearStatusDescription();
        layout(true);
    }

    public void scrollToTop()
    {
        setHScrollPosition(0);
        setVScrollPosition(0);
        if (wrapInPanel)
        {
            panel.setHScrollPosition(0);
            panel.setVScrollPosition(0);
        }
    }

    public void setHeading(String title)
    {
        if (wrapInPanel)
            panel.setHeading(title);
    }

    public FormDisplay getFormDisplay()
    {
        return formDisplay;
    }

    public void setFormDisplay(FormDisplay formDisplay)
    {
        this.formDisplay = formDisplay;
    }

    public void setButtonsDisplay(ButtonsDisplay buttonsDisplay)
    {
        this.buttonsDisplay = buttonsDisplay;
    }

    public ButtonsDisplay getButtonsDisplay()
    {
        return buttonsDisplay;
    }

    public NonSerOpt<ContentPanel> getPanel()
    {
        return NonSerOpt.option(panel);
    }
}
