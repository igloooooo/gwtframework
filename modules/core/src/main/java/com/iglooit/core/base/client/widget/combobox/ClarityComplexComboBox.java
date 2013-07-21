package com.iglooit.core.base.client.widget.combobox;

import com.clarity.core.base.client.PrivilegeResolver;
import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.core.base.client.widget.SecuredWidget;
import com.clarity.core.base.client.widget.SecuredWidgetDelegate;
import com.clarity.core.security.iface.access.domain.PrivilegeConst;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.store.ListStore;
import com.extjs.gxt.ui.client.widget.form.ComboBox;

import java.util.Collections;
import java.util.List;

/**
 * This combo box exists to avoid the limitations of ClarityComboBox extending SimpleComboBox
 *
 * @see ClarityComboBox does not support setDisplayField because it is a simpleComboBox and wraps simple objects
 */
public class ClarityComplexComboBox<T extends ModelData> extends ComboBox<T>
{
    private static final int MAX_WITH = 500;

    public ClarityComplexComboBox()
    {
        this(true, new String[0]);
    }

    /**
     * if needAdjustList is true, the dropdown list will automatically adjust its width to the
     * widest item in the list
     *
     * @param needAdjustListWidth if need adjust list width
     * @param properties          the properties in the model data that will be used to compare the longest width
     */
    public ClarityComplexComboBox(boolean needAdjustListWidth, final String[] properties)
    {
        this.getImages().setInvalid(Resource.ICONS.exclamationRed());
        this.setTriggerAction(ComboBox.TriggerAction.ALL);
        store = new ListStore<T>();

        if (needAdjustListWidth)
            ClarityComboUtil.enableDynamicSelectionWidthCalculation(this, properties);
    }

    public void selectFirst()
    {
        if (store.getCount() > 0)
        {
            setSelection(Collections.singletonList(store.getAt(0)));
        }
    }

    public void selectItem(String property, String value)
    {
        if (store.getCount() > 0)
        {
            T item = getStore().findModel(property, value);
            if (item != null)
            {
                setSelection(Collections.singletonList(item));
            }
        }
    }

    public void add(List<? extends T> models)
    {
        store.insert(models, store.getCount());
    }

    /**
     * @return try find the selected MD and return
     */
    public T getCurrentSelectedValue()
    {
        String currentValue = getRawValue();
        return findModel(getDisplayField(), currentValue);
    }

    public static class Secured<T extends ModelData> extends ClarityComplexComboBox<T> implements SecuredWidget
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
