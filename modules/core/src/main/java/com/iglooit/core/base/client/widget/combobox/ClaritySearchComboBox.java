package com.iglooit.core.base.client.widget.combobox;

import com.clarity.core.base.client.mvp.ClarityField;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.type.Option;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.List;

public class ClaritySearchComboBox implements ClarityField<String, SearchComboBox>
{
    private SearchComboBox combo;
    private HandlerManager innerHandlerManager = new HandlerManager(null);

    private String newValue = "";
    private String oldValue = "";

    public ClaritySearchComboBox()
    {
        combo = new SearchComboBox()
        {
            @Override
            public void collapse()
            {
                super.collapse();
                fireValueChangeEvent();
            }
        };
    }

    public void fireValueChangeEvent()
    {
        if (!fireChanged())
        {
            oldValue = newValue;
            ValueChangeEvent.fire(this, newValue);
        }
    }

    public boolean fireChanged()
    {
        if (combo.getValue() == null)
            newValue = "";
        else
            newValue = combo.getValue().toString();
        boolean fire = false;
        if (oldValue.equals(newValue))
            fire = true;
        return fire;
    }

    @Override
    public SearchComboBox getField()
    {
        return combo;
    }

    @Override
    public void handleValidationResults(List<ValidationResult> validationResultList)
    {

    }

    @Override
    public void valueExternallyChangedFrom(String oldLocalValue)
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
    public String getValue()
    {
        return combo.getValue() == null ? "" : combo.getValue().toString();
    }

    @Override
    public void setValue(String s)
    {
        combo.setRawValue(s);
    }

    @Override
    public void setValue(String s, boolean b)
    {
        combo.setRawValue(s);
        ValueChangeEvent.fire(this, s);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> stringValueChangeHandler)
    {
        final HandlerRegistration valueChangeReg =
            innerHandlerManager.addHandler(ValueChangeEvent.getType(), stringValueChangeHandler);
        return valueChangeReg;
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
