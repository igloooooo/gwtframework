package com.iglooit.core.base.client.widget.dialog;

import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;

/**
 * This dialog is intended to do the following:
 * 1. On Cancel, the dialog will simply be closed
 * 2. On Yes, the DataCapturedCallbackHandler will be called with the captured data
 * 3. On No, the dialog will remain open but will listen to success and failure event to get data to display
 *
 * @param <DTO>
 */
public abstract class EvalDialog<DTO, EVAL> extends AbstractDialogCapture<DTO>
{
    private EvalDataCapturedCallbackHandler evalDataCapturedCallbackHandler;
    public static final String EVALUATE = YES;
    public static final String ACCEPT = NO;

    protected EvalDialog(String heading)
    {
        super(heading, null);
    }

    public void setEvalDataCapturedCallbackHandler(EvalDataCapturedCallbackHandler handler)
    {
        this.evalDataCapturedCallbackHandler = handler;
    }

    @Override
    protected void configureButtons()
    {
        setButtons(YESNOCANCEL);

        Button button = getButtonById(CANCEL);
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

        button = getButtonById(ACCEPT);
        button.removeAllListeners();
        button.addSelectionListener(new SelectionListener<ButtonEvent>()
        {
            @Override
            public void componentSelected(ButtonEvent buttonEvent)
            {
                getDataCapturedCallbackHandler().dataCaptured(getCapturedData());
                registerSuccess();
            }
        });

        button = getButtonById(EVALUATE);
        button.removeAllListeners();
        button.addSelectionListener(new SelectionListener<ButtonEvent>()
        {
            @Override
            public void componentSelected(ButtonEvent buttonEvent)
            {
                mask();
                evalDataCapturedCallbackHandler.evalDataCaptured(getCapturedData());
            }
        });
    }

    protected abstract void setEvalData(EVAL evalData);

    public interface EvalDataCapturedCallbackHandler<DTO>
    {
        void evalDataCaptured(DTO dto);
    }
}