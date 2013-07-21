package com.iglooit.core.base.client.widget.form;

import com.clarity.core.account.iface.i18n.AccountViewConstants;
import com.clarity.core.base.client.mvp.Display;
import com.clarity.core.base.client.view.BaseViewConstants;
import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.commons.iface.type.AppX;
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

public class ButtonsDisplay implements Display
{
    private static final AccountViewConstants AVC = I18NFactoryProvider.get(AccountViewConstants.class);
    private static final BaseViewConstants BVC = I18NFactoryProvider.get(BaseViewConstants.class);

    private Button saveButton;
    private Button cancelButton;
    private ButtonBar buttonBar;

    //for button primarty mask
    private ButtonsDisplayButtonDetail buttonDetail;
    private List<Listener<? extends BaseEvent>> cancelListeners;
    private boolean hasCancel;
    private String defaultButtonStyle = ClarityStyle.BUTTON_PRIMARY;
    private String defaultButtonMaskMessage;
    private AbstractImagePrototype defaultButtonMaskIcon = Resource.ICONS.information();
    private String defaultButtonMaskStyle = ClarityStyle.BUTTON_LOADING;

    public ButtonsDisplay(final boolean showCancel)
    {
        this(showCancel, true);

    }

    public ButtonsDisplay(final boolean showCancel, boolean isPrimaryStyle)
    {
        if (BVC == null || AVC == null)
        {
            throw new AppX("Missing View Constants");
        }

        defaultButtonMaskMessage = BVC.saving();


        buttonBar = new ButtonBar();
        hasCancel = showCancel;

        saveButton = new Button()
        {
            @Override
            protected void onClick(ComponentEvent ce)
            {
                super.onClick(ce);
            }
        };
        saveButton.setText(AVC.saveButton());
        if (isPrimaryStyle)
            saveButton.addStyleName(defaultButtonStyle);
        saveButton.setIcon(Resource.ICONS.tick());

        if (showCancel)
        {
            cancelButton = new Button();
            cancelButton.setText(AVC.cancelButton());
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
        buttonDetail = new ButtonsDisplayButtonDetail(
            saveButton.getWidth(), hasCancel, saveButton.getText(),
            saveButton.getIcon(), style,
            saveButton.getListeners(Events.Select));

        //keep the following sequence of setting up button, otherwise may have problem of layout
        saveButton.setWidth(buttonDetail.getWidth());
        saveButton.setText(maskString);
        saveButton.removeStyleName(defaultButtonStyle);
        saveButton.addStyleName(style);
        saveButton.setIcon(IconHelper.create(ClarityStyle.ICON_PROGRESS_SPINNER));
        saveButton.removeAllListeners();
//        saveButton.disable();

//        if (hasCancel)
//        {
//            cancelButton.setText("");
//            cancelButton.setIcon(IconHelper.create(ClarityStyle.ICON_PROGRESS_SPINNER));
//            cancelListeners = cancelButton.getListeners(Events.OnClick);
//            cancelButton.removeAllListeners();
//        }
//        if (hasCancel)
//            cancelButton.hide();
        buttonBar.layout();
    }

    public void primaryProcessingUnmask()
    {
        //keep the following sequence of setting up button, otherwise may have problem of layout
        saveButton.setWidth(buttonDetail.getWidth());
        saveButton.setText(buttonDetail.getPreviousText());
        saveButton.removeStyleName(buttonDetail.getStyleName());
        saveButton.addStyleName(defaultButtonStyle);
        saveButton.setIcon(buttonDetail.getIcon());
        for (Listener<? extends BaseEvent> listener : buttonDetail.getClickListeners())
            saveButton.addListener(Events.Select, listener);
//        saveButton.enable();

//        if (hasCancel)
//        {
//            cancelButton.setText(AVC.cancelButton());
//            cancelButton.setIcon(null);
//            for (Listener<? extends BaseEvent> listener : cancelListeners)
//                cancelButton.addListener(Events.OnClick, listener);
//        }
//        if (hasCancel)
//            cancelButton.show();
        buttonBar.layout();
    }

    public void hide()
    {
        buttonBar.hide();
        buttonBar.layout();
    }

    public void show()
    {
        buttonBar.show();
        buttonBar.layout();
    }

    public void setButtonLoading(boolean enable)
    {
        saveButton.addStyleName(ClarityStyle.BUTTON_LOADING);
        saveButton.setIcon(IconHelper.create(ClarityStyle.ICON_PROGRESS_SPINNER));
    }

    public void removeSaveButtonPrimaryStyle()
    {
        saveButton.removeStyleName(defaultButtonStyle);
        this.layout();
    }

    public void layout()
    {
        buttonBar.layout();
    }

    public void addSaveButtonPrimaryStyle()
    {
        saveButton.addStyleName(defaultButtonStyle);
    }

    public void addNewButtonToButtonDislay(Button bt)
    {
        buttonBar.add(bt);
        layout();
    }

    public void applyReadModeButtonStyle()
    {
        removeSaveButtonPrimaryStyle();
        saveButton.setIcon(Resource.ICONS.edit());
    }
}
