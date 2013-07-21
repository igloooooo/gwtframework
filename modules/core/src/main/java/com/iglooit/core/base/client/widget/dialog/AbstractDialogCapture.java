package com.iglooit.core.base.client.widget.dialog;

import com.clarity.core.base.client.widget.htmlcontainer.MessageBoxHtml;
import com.clarity.core.base.client.widget.layoutcontainer.StatusDisplay;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.util.Margins;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.LayoutContainer;
import com.extjs.gxt.ui.client.widget.layout.FlowData;
import com.extjs.gxt.ui.client.widget.layout.FormLayout;

public abstract class AbstractDialogCapture<DTO> extends Dialog
{
    private LayoutContainer formPanel;
    private static final int DEFAULT_WIDTH = 400;
    private StatusDisplay statusDisplay;
    private DataCapturedCallbackHandler<DTO> dataCapturedCallbackHandler;

    public AbstractDialogCapture(String heading, DTO data)
    {
        statusDisplay = new StatusDisplay();
        setTopComponent(statusDisplay);
        statusDisplay.updateStatusDescription("", MessageBoxHtml.MessageType.ERROR);
        statusDisplay.setVisible(false);
        formPanel = new LayoutContainer(new FormLayout());
        add(formPanel, new FlowData(new Margins(5)));

        addComponents();
        if (data != null) setCapturedData(data);
        setWidth(DEFAULT_WIDTH);
        setHeading(heading);
        setModal(true);
        setHideOnButtonClick(false);
        setButtonAlign(Style.HorizontalAlignment.RIGHT);
        layout();
    }

    /**
     * Configure what kind of buttons will be shown
     */
    protected abstract void configureButtons();


    public void registerSuccess()
    {
        reset();
        hide();
    }

    protected void reset()
    {
        unmask();
        statusDisplay.setVisible(false);
        layout(true);
    }

    public void showError(String errorMessage)
    {
        statusDisplay.setVisible(true);
        statusDisplay.updateStatusDescription(errorMessage);
        unmask();
    }

    public LayoutContainer getFormPanel()
    {
        return formPanel;
    }

    protected abstract void addComponents();

    protected abstract void setCapturedData(DTO data);

    protected abstract DTO getCapturedData();

    public void setDataCapturedCallback(DataCapturedCallbackHandler<DTO> handler)
    {
        this.dataCapturedCallbackHandler = handler;
        ///todo pr: move in constructor/initialization or delete
        configureButtons();
    }

    public DataCapturedCallbackHandler<DTO> getDataCapturedCallbackHandler()
    {
        return dataCapturedCallbackHandler;
    }

    public interface DataCapturedCallbackHandler<DTO>
    {
        void dataCaptured(DTO dto);
    }
}