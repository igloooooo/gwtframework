package com.iglooit.core.base.client.widget.form;

import com.clarity.core.base.client.mvp.Display;
import com.clarity.core.base.client.view.BaseViewConstants;
import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.extjs.gxt.ui.client.widget.button.ButtonBar;
import com.google.gwt.user.client.ui.AbstractImagePrototype;
import com.google.gwt.user.client.ui.Widget;

import java.util.List;

public class AutoMaskButtonsDisplay implements Display
{
    private static final BaseViewConstants BASEVC = I18NFactoryProvider.get(BaseViewConstants.class);

    private Button saveButton;
    private ClarityConfirmButton cancelButton;
    private ButtonBar buttonBar;

    //for button primary mask
    private ButtonsDisplayButtonDetail buttonDetail;
    private List<Listener<? extends BaseEvent>> cancelListeners;
    private boolean hasCancel;
    private boolean masked = false;

    private String defaultButtonStyle = ClarityStyle.BUTTON_PRIMARY;
    private String defaultButtonMaskMessage = BASEVC.saving();
    private AbstractImagePrototype defaultButtonMaskIcon = Resource.ICONS.information();
    private String defaultButtonMaskStyle = ClarityStyle.BUTTON_LOADING;

    public AutoMaskButtonsDisplay(final boolean showCancel)
    {
        buttonBar = new ButtonBar();
        hasCancel = showCancel;

        saveButton = new Button()
        {
            @Override
            protected void onClick(ComponentEvent ce)
            {
                super.onClick(ce);
                buttonBar.layout();
            }
        };
        saveButton.setText(BASEVC.saveButton());
        saveButton.addStyleName(defaultButtonStyle);
        saveButton.setIcon(Resource.ICONS.tick());

        if (showCancel)
        {
            cancelButton = new ClarityConfirmButton();
            cancelButton.setText(BASEVC.cancelButton());
            cancelButton.setConfirmBoxTitle("No title provided");
            cancelButton.setConfirmBoxMessage("No message provided");
        }

        buttonBar.add(saveButton);
        if (showCancel)
            buttonBar.add(cancelButton);

    }


    public Widget asWidget()
    {
        return buttonBar;
    }

    public String getLabel()
    {
        return "";
    }

    public Button getSaveButton()
    {
        return saveButton;
    }

    public Button getCancelButton()
    {
        return cancelButton;
    }

    public void primaryProcessingMask()
    {
        primaryProcessingMask(defaultButtonMaskMessage, defaultButtonMaskIcon,
            defaultButtonMaskStyle);
    }

    public void primaryProcessingMask(String maskString, AbstractImagePrototype icon,
                                      String style)
    {
        if (masked)
            return;
        buttonDetail = new ButtonsDisplayButtonDetail(
            saveButton.getWidth(), hasCancel, saveButton.getText(),
            saveButton.getIcon(), saveButton.getStyleName(),
            saveButton.getListeners(Events.Select));

        //keep the following sequence of setting up button, otherwise may have problem of layout
        saveButton.setWidth(buttonDetail.getWidth());
        saveButton.setText(maskString);
        saveButton.removeStyleName(defaultButtonStyle);
        saveButton.addStyleName(style);
        saveButton.setIcon(IconHelper.create(ClarityStyle.ICON_PROGRESS_SPINNER));
        saveButton.enableEvents(false);
        masked = true;
        if (hasCancel)
            cancelButton.hide();
        this.layout();
    }


    public void primaryProcessingUnmask()
    {
        //keep the following sequence of setting up button, otherwise may have problem of layout
        if (!masked)
            return;
        if (buttonDetail == null)
            return;
        saveButton.setWidth(buttonDetail.getWidth());
        saveButton.setText(buttonDetail.getPreviousText());
        saveButton.removeStyleName(defaultButtonMaskStyle);
        saveButton.addStyleName(buttonDetail.getStyleName());
        saveButton.setIcon(buttonDetail.getIcon());
        saveButton.enableEvents(true);
        masked = false;

        if (hasCancel)
            cancelButton.show();
        this.layout();
    }

    public void hide()
    {
        buttonBar.hide();
    }

    public void setButtonLoading(boolean enable)
    {
        saveButton.addStyleName(ClarityStyle.BUTTON_LOADING);
        saveButton.setIcon(IconHelper.create(ClarityStyle.ICON_PROGRESS_SPINNER));
    }

    public void removeSaveButtonPrimaryStyle()
    {
        saveButton.removeStyleName(defaultButtonStyle);
    }

    public void layout()
    {
        buttonBar.layout();
    }

    public void setConfirmBoxTitleForCancel(String title)
    {
        cancelButton.setConfirmBoxTitle(title);
    }

    public void setConfirmBoxMessageForCancel(String message)
    {
        cancelButton.setConfirmBoxMessage(message);
    }
}
