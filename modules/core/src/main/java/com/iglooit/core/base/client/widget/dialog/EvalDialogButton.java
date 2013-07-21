package com.iglooit.core.base.client.widget.dialog;

import com.clarity.core.base.client.PrivilegeResolver;
import com.clarity.core.base.client.event.BasicPayloadEvent;
import com.clarity.core.base.client.event.RecoverableFailedEvent;
import com.clarity.core.base.client.widget.ClarityButton;
import com.clarity.core.base.client.widget.SecuredWidget;
import com.clarity.core.base.client.widget.SecuredWidgetDelegate;
import com.clarity.core.security.iface.access.domain.PrivilegeConst;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.ArrayList;

public class EvalDialogButton<DTO, EVAL> extends ClarityButton
{
    private EvalDialog<DTO, EVAL> dialog;

    public EvalDialogButton(String buttonLabel, final EvalDialog<DTO, EVAL> dialog)
    {
        super(buttonLabel);
        this.dialog = dialog;
    }

    public void setCapturedData(DTO capturedData)
    {
        dialog.setCapturedData(capturedData);
    }

    public ArrayList<HandlerRegistration> addEventHandlers(HandlerManager eventBus,
                                                           GwtEvent.Type<BasicPayloadEvent.Handler> succEventType,
                                                           GwtEvent.Type<RecoverableFailedEvent.Handler> failEventType)
    {
        final ArrayList<HandlerRegistration> list = new ArrayList();
        //to be able to listen to the dialog capture button itself
        list.add(addDialogCaptureButtonHandler());
        //when server-side update succeeded
        list.add(addSuccessEventHandler(eventBus, succEventType));
        //when server-side update failed
        list.add(addFailureEventHandler(eventBus, failEventType));

        return list;
    }

    public void setCapturedDataCallback(EvalDialog.DataCapturedCallbackHandler<DTO> handler)
    {
        dialog.setDataCapturedCallback(handler);
    }

    public void setEvalCapturedDataCallback(EvalDialog.EvalDataCapturedCallbackHandler<DTO> handler)
    {
        dialog.setEvalDataCapturedCallbackHandler(handler);
    }

    public HandlerRegistration addSuccessEventHandler(HandlerManager eventBus,
                                                      GwtEvent.Type<BasicPayloadEvent.Handler> type)
    {
        return eventBus.addHandler(type, new BasicPayloadEvent.Handler<BasicPayloadEvent<EVAL>>()
        {
            @Override
            public void handle(BasicPayloadEvent<EVAL> event)
            {
                dialog.setEvalData(event.getPayload());
                dialog.reset();
            }
        });
    }


    public HandlerRegistration addFailureEventHandler(HandlerManager eventBus,
                                                      GwtEvent.Type<RecoverableFailedEvent.Handler> type)
    {
        return eventBus.addHandler(type, new RecoverableFailedEvent.Handler<RecoverableFailedEvent<DTO>>()
        {
            @Override
            public void handle(RecoverableFailedEvent<DTO> event)
            {
                dialog.setCapturedData(event.getData());
                dialog.showError(event.getErrorMessage());
            }
        });
    }

    protected HandlerRegistration addDialogCaptureButtonHandler()
    {
        return addSelectionHandler(new SelectionHandler<ButtonEvent>()
        {
            @Override
            public void onSelection(SelectionEvent<ButtonEvent> buttonEventSelectionEvent)
            {
                dialog.show();
            }
        });
    }

    public static class Secured<DTO, EVAL> extends EvalDialogButton<DTO, EVAL> implements SecuredWidget
    {
        private SecuredWidgetDelegate delegate;

        public Secured(PrivilegeResolver resolver, PrivilegeConst privilege, String text,
                       EvalDialog<DTO, EVAL> dialogCapture)
        {
            super(text, dialogCapture);
            delegate = new SecuredWidgetDelegate(resolver, privilege);
            delegate.setSecuredWidget(this);
        }

        @Override
        public void setEnabled(boolean enabled)
        {
            delegate.setEnabled(enabled);
        }

        @Override
        public boolean isRendered()
        {
            return delegate.isRendered();
        }

        @Override
        public void superSetEnabled(boolean enabled)
        {
            super.setEnabled(enabled);
        }

        @Override
        public boolean superIsRendered()
        {
            return super.isRendered();
        }
    }

}