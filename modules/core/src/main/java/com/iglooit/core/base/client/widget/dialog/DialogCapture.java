package com.iglooit.core.base.client.widget.dialog;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;

public abstract class DialogCapture<DTO> extends AbstractDialogCapture<DTO>
{
    public DialogCapture(String heading, DTO data)
    {
        super(heading, data);
    }

    @Override
    protected void configureButtons()
    {
        setButtons(OKCANCEL);

        Button button = getButtonById(OK);
        button.removeAllListeners();
        button.addSelectionListener(new SelectionListener<ButtonEvent>()
        {
            @Override
            public void componentSelected(ButtonEvent buttonEvent)
            {
                mask();
                getDataCapturedCallbackHandler().dataCaptured(getCapturedData());
            }
        });

        button = getButtonById(CANCEL);
        button.removeAllListeners();
        button.addSelectionListener(new SelectionListener<ButtonEvent>()
        {
            @Override
            public void componentSelected(ButtonEvent buttonEvent)
            {
                hide();
                reset();
            }
        });

    }

}
