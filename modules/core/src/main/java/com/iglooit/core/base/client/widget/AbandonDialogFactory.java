package com.iglooit.core.base.client.widget;

import com.extjs.gxt.ui.client.event.SelectionListener;

public class AbandonDialogFactory
{
    public static final ClarityConfirmMessageBox createAbandonChangeDialog(
        SelectionListener confirmListener,
        SelectionListener cancelListener)
    {
        return new ClarityConfirmMessageBox(confirmListener, cancelListener)
        {
            @Override
            public void yesButtonClickHandler()
            {

            }
        };
    }
}
