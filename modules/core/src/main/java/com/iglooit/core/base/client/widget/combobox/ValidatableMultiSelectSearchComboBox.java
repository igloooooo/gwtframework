package com.iglooit.core.base.client.widget.combobox;

import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.type.Option;
import com.clarity.core.base.client.PrivilegeResolver;
import com.clarity.core.base.client.mvp.ClarityField;
import com.clarity.core.base.client.view.CommandPagingLoader;
import com.clarity.core.base.client.widget.SecuredWidget;
import com.clarity.core.base.client.widget.SecuredWidgetDelegate;
import com.clarity.core.security.iface.access.domain.PrivilegeConst;
import com.extjs.gxt.ui.client.data.ModelData;
import com.extjs.gxt.ui.client.event.BaseEvent;
import com.extjs.gxt.ui.client.event.EventType;
import com.extjs.gxt.ui.client.event.Listener;
import com.extjs.gxt.ui.client.store.ListStore;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ftsang
 * Date: 23/10/12
 * Time: 3:51 PM
 */
public class ValidatableMultiSelectSearchComboBox<MD extends ModelData>
    implements ClarityField<List<MD>, MultiSelectSearchComboBox>, IComboBox<MD>
{
    private MultiSelectSearchComboBox<MD> combo;
    private HandlerManager innerHandlerManager = new HandlerManager(null);

    private List<MD> oldValue = new ArrayList<MD>();
    private List<MD> newValue = new ArrayList<MD>();

    public ValidatableMultiSelectSearchComboBox()
    {
        combo = new MultiSelectSearchComboBox<MD>()
        {
            @Override
            public void collapse()
            {
                super.collapse();
                fireValueChangeEvent();
            }
        };
    }

    public boolean fireChanged()
    {
        newValue = combo.getSelectedObjectItems();
        boolean fire = false;
        if (!listEquals(oldValue, newValue))
            fire = true;
        return fire;
    }

    public void fireValueChangeEvent()
    {
        if (fireChanged())
        {
            oldValue = newValue;
            ValueChangeEvent.fire(this, newValue);
        }
    }

    @Override
    public MultiSelectSearchComboBox<MD> getField()
    {
        return combo;
    }

    @Override
    public void handleValidationResults(List<ValidationResult> validationResultList)
    {
    }

    @Override
    public void valueExternallyChangedFrom(List<MD> oldLocalValue)
    {
    }

    @Override
    public void setValue(List<MD> values)
    {
        combo.setSelectedItems(values);
    }

    public void setSelectedModels(List models)
    {
        combo.setSelectedItems(models);
    }

    @Override
    public void setValue(List<MD> values, boolean fireEvents)
    {
        setValue(values);
        fireValueChangeEvent();
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<List<MD>> listValueChangeHandler)
    {
        final HandlerRegistration valueChangeReg =
            innerHandlerManager.addHandler(ValueChangeEvent.getType(), listValueChangeHandler);
        return valueChangeReg;
    }

    @Override
    public String getFieldLabel()
    {
        return combo.getFieldLabel();
    }

    @Override
    public void setFieldLabel(String fieldLabel)
    {
        combo.setFieldLabel(fieldLabel);
    }

    @Override
    public Option<String> getUsageHint()
    {
        return Option.option(combo.getEmptyText());
    }

    @Override
    public void setUsageHint(String usageHint)
    {
        combo.setEmptyText(usageHint);
    }

    @Override
    public void fireEvent(GwtEvent<?> gwtEvent)
    {
        innerHandlerManager.fireEvent(gwtEvent);
    }

    @Override
    public void updateStore(List<MD> strings)
    {

    }

    @Override
    public void maskCombo()
    {

    }

    @Override
    public void unmaskCombo()
    {

    }

    @Override
    public List<MD> getSelectedItems()
    {
        return combo.getSelectedObjectItems();
    }

    public boolean listEquals(List<MD> oldList, List<MD> newList)
    {
        if (oldList.size() != newList.size())
        {
            return false;
        }
        for (MD newValue : newList)
        {
            if (!oldList.contains(newValue))
                return false;
        }
        return true;
    }

    @Override
    public List<MD> getValue()
    {
        return combo.getSelectedObjectItems();
    }

    public List getSelectedMetaModelData()
    {
        return combo.getSelectedObjectItems();
    }

    public ListStore getListStore()
    {
        return combo.getStore();
    }

    public void addListener(EventType eventType, Listener<? extends BaseEvent> listener)
    {
        combo.addListener(eventType, listener);
    }

    public void setPagingLoader(CommandPagingLoader asyncPagingLoader)
    {
        combo.setPagingLoader(asyncPagingLoader);
    }

    public static class Secured<D extends ModelData> extends ValidatableMultiSelectSearchComboBox<D> implements
        SecuredWidget
    {
        private SecuredWidgetDelegate delegate;

        public Secured(PrivilegeResolver resolver, PrivilegeConst privilege)
        {

            this.delegate = new SecuredWidgetDelegate(resolver, privilege);
            delegate.setSecuredWidget(this);
        }

        @Override
        public void addStyleName(String style)
        {
            getField().addStyleName("multi-select-search-combo");
        }

        @Override
        public void removeStyleName(String style)
        {
            getField().removeStyleName("multi-select-search-combo");
        }

        @Override
        public void setEnabled(boolean enabled)
        {
            delegate.setEnabled(enabled);
        }

        public boolean isRendered()
        {
            return delegate.isRendered();
        }

        @Override
        public void superSetEnabled(boolean enabled)
        {
            super.getField().setEnabled(enabled);
        }

        @Override
        public boolean superIsRendered()
        {
            return super.getField().isRendered();
        }
    }
}
