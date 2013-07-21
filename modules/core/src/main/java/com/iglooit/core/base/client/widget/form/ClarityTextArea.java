package com.iglooit.core.base.client.widget.form;

import com.clarity.core.base.client.PrivilegeResolver;
import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.core.base.client.widget.SecuredWidget;
import com.clarity.core.base.client.widget.SecuredWidgetDelegate;
import com.clarity.core.security.iface.access.domain.PrivilegeConst;
import com.extjs.gxt.ui.client.widget.form.TextArea;

public class ClarityTextArea extends TextArea
{
    public ClarityTextArea()
    {
        super();
        this.getImages().setInvalid(Resource.ICONS.exclamationRed());
    }

    public static class Secured extends ClarityTextArea implements SecuredWidget
    {
        private SecuredWidgetDelegate delegate;

        public Secured(PrivilegeResolver resolver, PrivilegeConst privilege, String label)
        {
            delegate = new SecuredWidgetDelegate(resolver, privilege);
            delegate.setSecuredWidget(this);
            setFieldLabel(label);
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
