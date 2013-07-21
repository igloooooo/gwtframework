package com.iglooit.core.base.client.widget;

import com.clarity.core.base.client.view.BaseViewConstants;
import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.commons.iface.util.StringUtil;
import com.extjs.gxt.ui.client.Style;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.MessageBoxEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.Dialog;
import com.extjs.gxt.ui.client.widget.MessageBox;

public abstract class ClarityConfirmMessageBox extends MessageBox
{
    protected static final BaseViewConstants BASEVC = I18NFactoryProvider.get(BaseViewConstants.class);

    public static enum ClarityMessageBoxType
    {
        YESNO, OK
    }

    /*all by default*/
    public ClarityConfirmMessageBox()
    {
        this(BASEVC.confirmMessageBoxDefaultTitle(), BASEVC.confirmMessageBoxDefaultMessage());

    }

    public void noButtonClickHandler()
    {
        //to be override
    }

    public abstract void yesButtonClickHandler();

    /*specify:
    *   title
    *   message*/
    public ClarityConfirmMessageBox(String title, String message)
    {
        this(title, message, ClarityStyle.ICON_QUESTION_32);
    }

    public ClarityConfirmMessageBox(SelectionListener confirmListener,
                                    SelectionListener cancelListener)
    {
        this.setTitle(BASEVC.abandonChangeDialogTitle());
        this.setMessage(BASEVC.abandonChangedMessage());
        this.setIcon(ClarityStyle.ICON_QUESTION_32);
        this.setButtons(MessageBox.YESNO);
        this.getDialog().setButtonAlign(Style.HorizontalAlignment.RIGHT);
        getDialog().getButtonById(Dialog.YES).addSelectionListener(confirmListener);
        getDialog().getButtonById(Dialog.NO).addSelectionListener(cancelListener);
    }

    /*specify:
    *   title
    *   message
    *   icon*/
    private ClarityConfirmMessageBox(String title, String message, String icon)
    {
        this(title, message, icon, ClarityMessageBoxType.YESNO);
    }

    /*specify:
    *   title
    *   message
    *   icon
    *   type*/
    private ClarityConfirmMessageBox(String title, String message, String icon, ClarityMessageBoxType type)
    {
        super();
        this.addCallback(new Listener<MessageBoxEvent>()
        {
            public void handleEvent(MessageBoxEvent be)
            {
                if (be.getButtonClicked().getItemId().equalsIgnoreCase(Dialog.YES))
                {
                    yesButtonClickHandler();
                }
                if (be.getButtonClicked().getText().toLowerCase().equals(Dialog.NO.toLowerCase()))
                {
                    noButtonClickHandler();
                }
            }
        });
        if (StringUtil.isEmpty(title))
            this.setTitle(BASEVC.confirmMessageBoxDefaultTitle());
        else
            this.setTitle(title);

        if (StringUtil.isEmpty(message))
            this.setMessage(BASEVC.confirmMessageBoxDefaultMessage());
        else
            this.setMessage(message);
        this.setIcon(icon);
        if (type == ClarityMessageBoxType.YESNO)
            this.setButtons(MessageBox.YESNO);
        else
            this.setButtons(MessageBox.OK);
        this.getDialog().setButtonAlign(Style.HorizontalAlignment.RIGHT);
    }
}
