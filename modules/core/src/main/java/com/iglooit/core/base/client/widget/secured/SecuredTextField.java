package com.iglooit.core.base.client.widget.secured;

import com.clarity.core.base.client.PrivilegeResolver;
import com.clarity.core.base.client.widget.SecuredWidget;
import com.clarity.core.base.client.widget.SecuredWidgetDelegate;
import com.clarity.core.security.iface.access.domain.PrivilegeConst;
import com.extjs.gxt.ui.client.widget.form.TextField;

public class SecuredTextField<D> extends TextField<D> implements SecuredWidget
{
    private SecuredWidgetDelegate delegate;

    public SecuredTextField(PrivilegeResolver resolver, PrivilegeConst privilege, String fieldLabel)
    {
        delegate = new SecuredWidgetDelegate(resolver, privilege);
        delegate.setSecuredWidget(this);
        setFieldLabel(fieldLabel);
    }

    @Override
    public void setEnabled(boolean enabled)
    {
        delegate.setEnabled(enabled);
    }

    @Override
    public boolean isRendered()
    {
        return delegate == null ? false : delegate.isRendered();
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
