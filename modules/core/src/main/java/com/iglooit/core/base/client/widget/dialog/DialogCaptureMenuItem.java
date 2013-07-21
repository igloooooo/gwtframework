package com.iglooit.core.base.client.widget.dialog;

import com.clarity.core.base.client.PrivilegeResolver;
import com.clarity.core.base.client.event.BasicPayloadEvent;
import com.clarity.core.base.client.event.DomainEntityLoadEvent;
import com.clarity.core.base.client.event.RecoverableFailedEvent;
import com.clarity.core.base.client.widget.SecuredWidget;
import com.clarity.core.base.client.widget.SecuredWidgetDelegate;
import com.clarity.core.security.iface.access.domain.PrivilegeConst;
import com.extjs.gxt.ui.client.event.MenuEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.menu.MenuItem;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.ArrayList;

public class DialogCaptureMenuItem<DTO> extends MenuItem
{
    private DialogCapture dialogCapture;

    public DialogCaptureMenuItem(String text, final DialogCapture<DTO> dialogCapture)
    {
        super(text);
        this.dialogCapture = dialogCapture;
    }

    public DialogCapture getDialogCapture()
    {
        return dialogCapture;
    }

    public void setCapturedData(DTO capturedData)
    {
        dialogCapture.setCapturedData(capturedData);
    }

    //todo pr: make success event type more generic
    public ArrayList<HandlerRegistration> addEventHandlers(HandlerManager eventBus,
                                                           GwtEvent.Type<DomainEntityLoadEvent.Handler> succEventType,
                                                           GwtEvent.Type<RecoverableFailedEvent.Handler> failEventType)
    {
        final ArrayList<HandlerRegistration> list = new ArrayList<HandlerRegistration>();
        //to be able to listen to the dialog capture button itself
        addDialogCaptureButtonHandler();
        //when server-side update succeeded
        list.add(addSuccessEventHandler(eventBus, succEventType));
        //when server-side update failed
        list.add(addFailureEventHandler(eventBus, failEventType));

        return list;
    }

    public void setCapturedDataCallback(DialogCapture.DataCapturedCallbackHandler<DTO> handler)
    {
        dialogCapture.setDataCapturedCallback(handler);
    }


    private HandlerRegistration addSuccessEventHandler(HandlerManager eventBus,
                                                       GwtEvent.Type<BasicPayloadEvent.Handler> type)
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


    private HandlerRegistration addFailureEventHandler(HandlerManager eventBus,
                                                       GwtEvent.Type<RecoverableFailedEvent.Handler> type)
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

    //todo at: Add handler registration here
    private void addDialogCaptureButtonHandler()
    {
        addSelectionListener(new SelectionListener<MenuEvent>()
        {
            @Override
            public void componentSelected(MenuEvent menuEvent)
            {
                dialogCapture.show();
            }
        });
    }

    public static class Secured<DTO> extends DialogCaptureMenuItem<DTO> implements SecuredWidget
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
