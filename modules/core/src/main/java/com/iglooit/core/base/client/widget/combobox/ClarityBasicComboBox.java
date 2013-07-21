package com.iglooit.core.base.client.widget.combobox;

import com.clarity.core.base.client.PrivilegeResolver;
import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.widget.SecuredWidget;
import com.clarity.core.base.client.widget.SecuredWidgetDelegate;
import com.clarity.core.security.iface.access.domain.PrivilegeConst;
import com.extjs.gxt.ui.client.core.El;

public class ClarityBasicComboBox<T> extends ClarityComboBox<T>
{
    private static final int COMBO_WIDTH = 200;

    public ClarityBasicComboBox()
    {
        super();
        setEditable(false);
        setWidth(COMBO_WIDTH);
    }

    @Override
    public El mask()
    {
        return super.mask("", ClarityStyle.MASK_FIELD);
    }

    public static class Secured<T> extends ClarityBasicComboBox<T> implements SecuredWidget
    {
        private SecuredWidgetDelegate delegate;

        public Secured(PrivilegeResolver resolver, PrivilegeConst privilege)
        {

            this.delegate = new SecuredWidgetDelegate(resolver, privilege);
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
