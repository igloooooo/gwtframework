package com.iglooit.core.base.client.widget.form;

import com.clarity.core.base.client.view.ClarityStyle;
import com.extjs.gxt.ui.client.util.IconHelper;
import com.extjs.gxt.ui.client.widget.button.Button;

public class AutoMaskButton extends Button
{
    private ButtonsDisplayButtonDetail buttonDetail;
    private boolean mask = false;

    public AutoMaskButton()
    {
    }

    public AutoMaskButton(String text)
    {
        super(text);
    }

    public void processMask()
    {
        processMask(getText());
    }

    public void processMask(String maskString)
    {
        if (mask)
            return;
        buttonDetail = new ButtonsDisplayButtonDetail(
            getWidth(), false, getText(),
            getIcon(), getStyleName(),
            null);
        setText(maskString);
        setIcon(IconHelper.create(ClarityStyle.ICON_PROGRESS_SPINNER));
        addStyleName(ClarityStyle.BUTTON_LOADING);
        enableEvents(false);
        mask = true;
    }

    public void processUnmask()
    {
        if (!mask || buttonDetail == null)
            return;
        setText(buttonDetail.getPreviousText());
        removeStyleName(ClarityStyle.BUTTON_LOADING);
        if (buttonDetail.getIcon() != null)
        {
            setIcon(buttonDetail.getIcon());
        }
        else
        {
            setIcon(null);
            // remove style to render correctly when no icon was set initially
            removeStyleName("x-btn-text-icon");
        }
        enableEvents(true);
        setWidth(buttonDetail.getWidth());
        mask = false;
    }
}
