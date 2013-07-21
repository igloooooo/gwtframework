package com.iglooit.core.base.client.widget;

import com.clarity.core.base.client.PrivilegeResolver;
import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.security.iface.access.domain.PrivilegeConst;

public class SecuredWidgetDelegate
{
    private PrivilegeResolver resolver;
    private PrivilegeConst privilege;
    private SecuredWidget securedWidget;
    private boolean allowed;
    private boolean userOverride;

    public SecuredWidgetDelegate(PrivilegeResolver resolver, PrivilegeConst privilege)
    {
        this.resolver = resolver;
        this.privilege = privilege;
    }

    public void setSecuredWidget(SecuredWidget securedWidget)
    {
        this.securedWidget = securedWidget;
        setEnabled(true);
    }

    public boolean isRendered()
    {
        if (securedWidget.superIsRendered() && allowed != resolver.hasPrivilege(privilege))
        {
            allowed = !allowed;
            setEnabledState(allowed && userOverride);
        }
        return securedWidget.superIsRendered();
    }

    public void setEnabled(boolean enabled)
    {
        allowed = resolver.hasPrivilege(privilege);
        userOverride = enabled;
        setEnabledState(userOverride && allowed);
    }

    private void setEnabledState(boolean enabled)
    {
        securedWidget.removeStyleName(ClarityStyle.SECURE_EDITABLE_WIDGET);
        securedWidget.removeStyleName(ClarityStyle.SECURE_READONLY_WIDGET);
        securedWidget.addStyleName(allowed ? ClarityStyle.SECURE_EDITABLE_WIDGET : ClarityStyle.SECURE_READONLY_WIDGET);
        securedWidget.superSetEnabled(enabled && allowed);
    }

    public void setPrivilege(PrivilegeConst privilege)
    {
        this.privilege = privilege;
        setEnabledState(userOverride);
    }
}
