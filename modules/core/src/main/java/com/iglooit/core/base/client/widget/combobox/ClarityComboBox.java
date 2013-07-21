package com.iglooit.core.base.client.widget.combobox;

import com.clarity.core.base.client.PrivilegeResolver;
import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.core.base.client.widget.SecuredWidget;
import com.clarity.core.base.client.widget.SecuredWidgetDelegate;
import com.clarity.core.security.iface.access.domain.PrivilegeConst;
import com.extjs.gxt.ui.client.event.ComponentEvent;
import com.extjs.gxt.ui.client.event.Events;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.event.SelectionChangedEvent;
import com.extjs.gxt.ui.client.widget.form.ComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboBox;
import com.extjs.gxt.ui.client.widget.form.SimpleComboValue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Avoid using this class - use ClarityComplexComboBox instead. This class extends gxt's ridiculous SimpleComboBox
 * which has a ridiculous implementaton of setValue(..).
 */
public class ClarityComboBox<T> extends SimpleComboBox<T>
{
    private int listZIndex = -1;
    private Listener<ComponentEvent> dynamicComboWidthListener;

    public ClarityComboBox()
    {
        super();
        this.getImages().setInvalid(Resource.ICONS.exclamationRed());
        this.setTriggerAction(ComboBox.TriggerAction.ALL);
        dynamicComboWidthListener = ClarityComboUtil.enableDynamicSelectionWidthCalculation(this);
//
//        this.addListener(Events.Blur, new Listener<BaseEvent>()
//        {
//            @Override
//            public void handleEvent(BaseEvent baseEvent)
//            {
//                fireSelectionChangedEvent();
//            }
//        });
    }

    public void disableDynamicComboWidthListener()
    {
        if (dynamicComboWidthListener != null)
            ClarityComboUtil.disableDynamicSelectionWidthCalculation(this, dynamicComboWidthListener);
    }

    @Override
    protected void onTriggerClick(ComponentEvent ce)
    {
        super.onTriggerClick(ce);
        if (listZIndex != -1)
        {
            getListView().el().getParent().setZIndex(listZIndex);
        }
    }

    public List<T> getStoreList()
    {
        List<T> list = new ArrayList<T>();
        for (int i = 0; i < store.getCount(); i++)
        {
            list.add(store.getAt(i).getValue());
        }
        return list;
    }

    /**
     * Forces the selection change to fire based on the current selection - might be needed for triggering refresh on
     * the selection listeners of this combobox
     */
    public void fireSelectionChangedEvent()
    {
        fireEvent(Events.SelectionChange, new SelectionChangedEvent<SimpleComboValue<T>>(this, getSelection()));
    }

    public void selectFirst()
    {
        if (store.getCount() > 0)
        {
            setSelection(Collections.singletonList(store.getAt(0)));
        }
        else
        {
            clear();
        }
    }

    public void selectItem(T value)
    {
        if (store.getCount() > 0)
        {
            SimpleComboValue<T> item = getStore().findModel("value", value);
            if (item != null)
            {
                setSelection(Collections.singletonList(item));
            }
        }
    }

    public boolean containValue(T value)
    {
        return (value != null && getStoreList().contains(value));
    }

    public void setValueOfFirstItem()
    {
        if (store.getCount() > 0)
            setValue(store.getAt(0));
    }

    public void setValueAsThis(T value)
    {
        if (value == null)
            return;
        for (int i = 0; i < store.getCount(); i++)
        {
            if (store.getAt(i).getValue().equals(value))
            {
                setValue(store.getAt(i));
                return;
            }
        }
    }

    public T getCurrentSelectedValue()
    {
        if (getValue() != null)
            return getValue().getValue();
        else
            return null;
    }

    public void setFirstIfThisItemNotInStore(T value)
    {
        if (!containValue(value))
            setValueOfFirstItem();
        else
            setValueAsThis(value);
    }

    public void updateStoreList(List<T> list)
    {
        unmask();
        removeAll();
        add(list);
    }

    public void loadingMask()
    {
        this.mask("", ClarityStyle.MASK_FIELD);
    }

    public void setListZIndex(int listZIndex)
    {
        this.listZIndex = listZIndex;
    }

    public void selectExistingItemImmediate(T itemToSelect)
    {
        SimpleComboValue<T> comboValue = findModel(itemToSelect);
        if (comboValue != null) setSelection(Collections.singletonList(comboValue));
    }

    public static class Secured<T> extends ClarityComboBox<T> implements SecuredWidget
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
