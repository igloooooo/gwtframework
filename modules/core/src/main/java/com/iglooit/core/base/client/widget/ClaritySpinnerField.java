package com.iglooit.core.base.client.widget;

import com.clarity.core.base.client.PrivilegeResolver;
import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.core.security.iface.access.domain.PrivilegeConst;
import com.extjs.gxt.ui.client.widget.form.SpinnerField;


public class ClaritySpinnerField extends SpinnerField
{
    public ClaritySpinnerField()
    {
        super();
        this.getImages().setInvalid(Resource.ICONS.exclamationRed());
    }


    public static class Secured extends ClaritySpinnerField
    {
        private PrivilegeResolver privilegeResolver;
        private PrivilegeConst privilege;
        //used to detect change in privilege
        private boolean allowed;
        private boolean userEnabled;

        public Secured(PrivilegeResolver privilegeResolver, PrivilegeConst privilege)
        {
            super();
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
