package com.iglooit.core.base.client.widget;

import com.clarity.core.base.client.PrivilegeResolver;
import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.security.iface.access.domain.PrivilegeConst;
import com.extjs.gxt.ui.client.event.ButtonEvent;
import com.extjs.gxt.ui.client.event.SelectionListener;
import com.extjs.gxt.ui.client.widget.button.Button;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;

public class ClarityButton<T> extends Button implements HasSelectionHandlers<T>, HasClickHandlers
{
    public ClarityButton(String s)
    {
        super(s);
    }

    private HasSelectionHandlers<T> getHSHRepr()
    {
        return this;
    }

    public HandlerRegistration addSelectionHandler(final SelectionHandler<T> handler)
    {
        final HandlerRegistration widgetListener = addHandler(handler, SelectionEvent.getType());
        final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>()
        {
            public void componentSelected(ButtonEvent buttonEvent)
            {
                SelectionEvent.fire(getHSHRepr(), null);
            }
        };

        addSelectionListener(listener);

        return new HandlerRegistration()
        {
            public void removeHandler()
            {
                removeSelectionListener(listener);
                widgetListener.removeHandler();
            }
        };
    }

    public HandlerRegistration addClickHandler(ClickHandler clickHandler)
    {
        final HandlerRegistration widgetListener = addHandler(clickHandler, ClickEvent.getType());
        final SelectionListener<ButtonEvent> listener = new SelectionListener<ButtonEvent>()
        {
            public void componentSelected(ButtonEvent buttonEvent)
            {
                SelectionEvent.fire(getHSHRepr(), null);
            }
        };

        addSelectionListener(listener);

        return new HandlerRegistration()
        {
            public void removeHandler()
            {
                removeSelectionListener(listener);
                widgetListener.removeHandler();
            }
        };
    }

    @Override
    public void onBrowserEvent(Event event)
    {
        int eventType = DOM.eventGetType(event);
        if (disabled && (eventType == Event.ONMOUSEOVER || eventType == Event.ONMOUSEOUT))
        {
            enable();
            super.onBrowserEvent(event);
            disable();
        }
        else
        {
            super.onBrowserEvent(event);
        }
    }

    public static class Secured extends ClarityButton implements SecuredWidget
    {
        private PrivilegeResolver privilegeResolver;
        private PrivilegeConst privilege;
        //used to detect change in privilege
        private boolean allowed;
        private boolean userEnabled;

        public Secured(PrivilegeResolver privilegeResolver, PrivilegeConst privilege, String text)
        {
            super(text);
            this.privilegeResolver = privilegeResolver;
            this.privilege = privilege;
            setEnabled(true);
        }

        private void setEnabledState(boolean enabled)
        {
            removeStyleName(ClarityStyle.SECURE_EDITABLE_WIDGET);
            removeStyleName(ClarityStyle.SECURE_READONLY_WIDGET);
            addStyleName(allowed ? ClarityStyle.SECURE_EDITABLE_WIDGET : ClarityStyle.SECURE_READONLY_WIDGET);
            super.setEnabled(enabled && allowed);
        }

        @Override
        public void setEnabled(boolean enabled)
        {
            allowed = privilegeResolver.hasPrivilege(privilege);
            userEnabled = enabled;
            setEnabledState(userEnabled && allowed);
        }

        @Override
        public boolean isRendered()
        {
            if (rendered && allowed != privilegeResolver.hasPrivilege(privilege))
            {
                allowed = !allowed;
                setEnabledState(allowed && userEnabled);
            }
            return super.isRendered();
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
