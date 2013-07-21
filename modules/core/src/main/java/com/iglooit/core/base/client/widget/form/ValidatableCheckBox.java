package com.iglooit.core.base.client.widget.form;

import com.clarity.core.base.client.mvp.ClarityField;
import com.clarity.core.base.client.mvp.DelayedTimeValidationResultHandler;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.type.Option;
import com.clarity.commons.iface.util.StringUtil;
import com.extjs.gxt.ui.client.widget.form.CheckBox;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.List;

public class ValidatableCheckBox extends CheckBox implements ClarityField<Boolean, CheckBox>
{
    private boolean fireChangeEvents = true;
    private final DelayedTimeValidationResultHandler<Boolean> timeErrorHandler;

    public ValidatableCheckBox()
    {
        super();
        this.timeErrorHandler = new DelayedTimeValidationResultHandler<Boolean>(this, new Runnable()
        {
            public void run()
            {
                fireValidationChangeEvent();
            }
        });
    }

    private void fireValidationChangeEvent()
    {
        if (fireChangeEvents)
        {
            Boolean value = getValue();
            ValueChangeEvent.fire(ValidatableCheckBox.this, value);
        }
    }

    @Override
    public CheckBox getField()
    {
        return this;
    }

    @Override
    public void handleValidationResults(List<ValidationResult> validationResultList)
    {
        timeErrorHandler.handleValidationResults(validationResultList);
    }

    @Override
    public void valueExternallyChangedFrom(Boolean oldLocalValue)
    {
        //nothing
    }

    @Override
    public Option<String> getUsageHint()
    {
        return Option.option(getEmptyText());
    }

    @Override
    public void setUsageHint(String usageHint)
    {
        setEmptyText(usageHint);
    }

    @Override
    public void setValue(Boolean aBoolean, boolean b)
    {
        fireChangeEvents = b;
        super.setValue(aBoolean);
        fireValidationChangeEvent();
        fireChangeEvents = true;
    }

    @Override
    public void setValue(Boolean d)
    {
        setValue(d, true);
    }

    @Override
    public Boolean getValue()
    {
        return this.value;
    }

    @Override
    public boolean validate()
    {
        // only use binder validator, not gxt
        return true;
    }

    @Override
    public HandlerRegistration addValueChangeHandler(final ValueChangeHandler<Boolean> booleanValueChangeHandler)
    {
        return timeErrorHandler.addValueChangeHandler(booleanValueChangeHandler);

    }

    @Override
    public String getFieldLabel()
    {
        return StringUtil.emptyStringIfNull(super.getFieldLabel());
    }

    @Override
    public void setFieldLabel(String fieldLabel)
    {
        super.setFieldLabel(fieldLabel);
        setBoxLabel(fieldLabel);
    }

    @Override
    public void fireEvent(GwtEvent<?> gwtEvent)
    {
        timeErrorHandler.fireEvent(gwtEvent);
    }
}
