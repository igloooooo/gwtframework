package com.iglooit.core.base.client.widget.dialog;

import com.clarity.core.base.client.PrivilegeResolver;
import com.clarity.core.base.client.event.BasicPayloadEvent;
import com.clarity.core.base.client.event.DomainEntityLoadEvent;
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

public class DialogCaptureButton<DTO> extends ClarityButton
{
    private DialogCapture dialogCapture;

    public DialogCaptureButton(String buttonLabel, final DialogCapture<DTO> dialogCapture)
    {
        super(buttonLabel);
        this.dialogCapture = dialogCapture;
    }

    public void setCapturedData(DTO capturedData)
    {
        dialogCapture.setCapturedData(capturedData);
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

        HandlerRegistration failedRegistration = addFailureEventHandler(eventBus, failEventType);
        if (failedRegistration != null)
            list.add(failedRegistration);

        return list;
    }

    public void setCapturedDataCallback(DialogCapture.DataCapturedCallbackHandler<DTO> handler)
    {
        dialogCapture.setDataCapturedCallback(handler);
    }


    public HandlerRegistration addSuccessEventHandler(HandlerManager eventBus,
                                                      GwtEvent.Type<DomainEntityLoadEvent.Handler> type)
    {
        return eventBus.addHandler(type, new BasicPayloadEvent.Handler()
        {
            @Override
            public void handle(BasicPayloadEvent event)
            {
                dialogCapture.registerSuccess();
            }
        });
    }


    public HandlerRegistration addFailureEventHandler(HandlerManager eventBus,
                                                      GwtEvent.Type<RecoverableFailedEvent.Handler> type)
    {
        if (type != null)
        {
            return eventBus.addHandler(type, new RecoverableFailedEvent.Handler<RecoverableFailedEvent>()
            {
                @Override
                public void handle(RecoverableFailedEvent event)
                {
                    dialogCapture.setCapturedData(event.getData());
                    dialogCapture.showError(event.getErrorMessage());
                }
            });
        }
        else
            return null;
    }

    protected HandlerRegistration addDialogCaptureButtonHandler()
    {
        return addSelectionHandler(new SelectionHandler<ButtonEvent>()
        {
            @Override
            public void onSelection(SelectionEvent<ButtonEvent> buttonEventSelectionEvent)
            {
                dialogCapture.show();
            }
        });
    }

    public static class Secured<DTO> extends DialogCaptureButton<DTO> implements SecuredWidget
    {
        private SecuredWidgetDelegate delegate;

        public Secured(PrivilegeResolver resolver, PrivilegeConst privilege, String text,
                       DialogCapture<DTO> dialogCapture)
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
