package com.iglooit.core.base.client.widget.form;

import com.clarity.core.base.client.PrivilegeResolver;
import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.core.security.iface.access.domain.PrivilegeConst;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.FieldEvent;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.google.gwt.dom.client.NativeEvent;

public class ClarityNumberField extends NumberField
{
    public ClarityNumberField()
    {
        super();
        this.getImages().setInvalid(Resource.ICONS.exclamationRed());
    }

    @Override
    protected void onKeyPress(FieldEvent fe)
    {
        fireEvent(Events.KeyPress, new FieldEvent(this, fe.getEvent()));

        if (fe.isSpecialKey(getKeyCode(fe.getEvent())) || fe.isControlKey())
        {
            return;
        }

        char key = getChar(fe.getEvent());

        if (!allowed.contains(key))
        {
            fe.stopEvent();
        }
    }

    private native char getChar(NativeEvent e) /*-{
            return e.which || e.charCode || e.keyCode || 0;
    }-*/;

    private native int getKeyCode(NativeEvent e) /*-{
            return e.keyCode || 0;
    }-*/;

    public static class Secured extends ClarityNumberField
    {
        private PrivilegeResolver privilegeResolver;
        private PrivilegeConst privilege;
        //used to detect change in privilege
        private boolean allowed;
        private boolean userEnabled;

        public Secured(PrivilegeResolver privilegeResolver, PrivilegeConst privilege, int text)
        {
            super();
            setValue(text);
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
    }
}
