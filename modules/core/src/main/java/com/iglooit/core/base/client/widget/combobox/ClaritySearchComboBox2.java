package com.iglooit.core.base.client.widget.combobox;

import com.clarity.core.base.client.model.SimpleModelValue;
import com.clarity.core.base.client.mvp.ClarityField;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.type.Option;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.List;

public class ClaritySearchComboBox2<M, MD extends SimpleModelValue<M>> implements ClarityField<M, SearchComboBox<MD>>
{
    private SearchComboBox<MD> combo;
    private HandlerManager innerHandlerManager = new HandlerManager(null);

    private SimpleModelValue<M> oldValue;
    private SimpleModelValue<M> newValue;
    private boolean forceSet = false;

    public ClaritySearchComboBox2()
    {
        combo = new SearchComboBox<MD>()
        {
            @Override
            public void collapse()
            {
                super.collapse();
                fireValueChangeEvent();
            }
        };
        // only been used for local search to prevent not loading at first hit when value is null
        combo.setUseQueryCache(false);
    }

    public boolean isForceSet()
    {
        return forceSet;
    }

    /***
     * Whether forces setting of combo box value in setValue call. This affects getValue when the list hasn't
     * been populated to ensure a value is returned if force value is false.
     * @param forceSet
     */
    public void setForceSet(boolean forceSet)
    {
        this.forceSet = forceSet;
    }

    public void fireValueChangeEvent()
    {
        if (fireChanged())
        {
            ValueChangeEvent.fire(this, newValue.getValue());
        }
    }

    public boolean fireChanged()
    {
        if (combo.getValue() == null)
            return false;

        newValue = combo.getValue();
        boolean fire = false;
        if (oldValue == null || !oldValue.equals(newValue))
            fire = true;
        return fire;
    }

    @Override
    public SearchComboBox<MD> getField()
    {
        return combo;
    }

    @Override
    public void handleValidationResults(List<ValidationResult> validationResultList)
    {

    }

    @Override
    public void valueExternallyChangedFrom(M oldLocalValue)
    {

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
    public M getValue()
    {
        return combo.getValue() == null ? null : combo.getValue().getValue();
    }

    @Override
    public void setValue(M m)
    {
        if (m == null)
        {
            combo.setRawValue("");
            return;
        }
        combo.setRawValue(m.toString());
        if (combo.hasNoElement())
            return;
        
        boolean isSet = false;
        for (MD md : combo.getStore().getModels())
        {
            if (md.getValue().equals(m))
            {
                oldValue = md;
                combo.setValue(md);
                isSet = true;
            }
        }

        if (!isSet && forceSet)
        {
            oldValue = new SimpleModelValue<M>(m);
            oldValue.setDisplayValue((String)m);
            combo.setValue((MD)oldValue);
        }
    }

    @Override
    public void setValue(M s, boolean b)
    {
        this.setValue(s);
        ValueChangeEvent.fire(this, s);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<M> stringValueChangeHandler)
    {
        return innerHandlerManager.addHandler(ValueChangeEvent.getType(), stringValueChangeHandler);
    }

    @Override
    public void fireEvent(GwtEvent<?> gwtEvent)
    {
        innerHandlerManager.fireEvent(gwtEvent);
    }

    public void setDisplayField(String displayField)
    {
        combo.getPropertyEditor().setDisplayProperty(displayField);
    }
}
