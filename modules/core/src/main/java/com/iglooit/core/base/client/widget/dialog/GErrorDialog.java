package com.iglooit.core.base.client.widget.dialog;

import com.extjs.gxt.ui.client.widget.Dialog;
import com.google.gwt.user.client.Element;

public class GErrorDialog extends Dialog
{
    public GErrorDialog(String title, String errorMessage, String stackTrace)
    {
        setHeading(title);
        addText(errorMessage + "\n" + stackTrace);
        setHideOnButtonClick(true);
        setButtons(Dialog.OK);
    }

    @Override
    protected final void onRender(Element element, int i)
    {
        super.onRender(element, i);
        center();
    }

}
