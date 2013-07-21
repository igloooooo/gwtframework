package com.iglooit.core.base.client.widget.form;

import com.clarity.core.base.client.mvp.ClarityField;
import com.clarity.core.base.client.mvp.DelayedTimeValidationResultHandler;
import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.type.Option;
import com.clarity.commons.iface.util.StringUtil;
import com.extjs.gxt.ui.client.widget.form.NumberField;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;

import java.util.List;

public class ValidatableNumberField implements ClarityField<String, NumberField>
{
    private NumberField numberField;
    private boolean fireChangeEvents = true;
    private final DelayedTimeValidationResultHandler<String> timeErrorHandler;

    public ValidatableNumberField()
    {
        numberField = new NumberField();
        numberField.getImages().setInvalid(Resource.ICONS.exclamationRed());
        getField().setFireChangeEventOnSetValue(false);
        timeErrorHandler = new DelayedTimeValidationResultHandler<String>(this, new Runnable()
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
            String value = getValue();
            ValueChangeEvent.fire(ValidatableNumberField.this, value);
        }
    }

    public static final int VALIDATE_DELAY_MILLIS = 500;

    private boolean typingTimeout = false;
    private String validationShowErrorsMessage = "";

    private final Timer validationShowErrorsTimer = new Timer()
    {
        public void run()
        {
            numberField.forceInvalid(validationShowErrorsMessage);
        }
    };

    @Override
    public NumberField getField()
    {
        return numberField;
    }

    @Override
    public void fireEvent(GwtEvent<?> gwtEvent)
    {
        timeErrorHandler.fireEvent(gwtEvent);
    }

    @Override
    public String getFieldLabel()
    {
        return StringUtil.emptyStringIfNull(numberField.getFieldLabel());
    }

    @Override
    public void setFieldLabel(String fieldLabel)
    {
        numberField.setFieldLabel(fieldLabel);
    }

    @Override
    public String getValue()
    {
        Number superValue = numberField.getValue();
        if (superValue == null)
            return "";
        return superValue.toString();
    }

    @Override
    public void setValue(String d)
    {
        Number number = Double.valueOf(d);
        numberField.setValue(number);
    }

    public void setValue(String d, boolean b)
    {
        fireChangeEvents = b;
        setValue(d);
        fireValidationChangeEvent();
        fireChangeEvents = true;
    }

    @Override
    public void handleValidationResults(List<ValidationResult> validationResultList)
    {
        timeErrorHandler.handleValidationResults(validationResultList);
    }

    @Override
    public void valueExternallyChangedFrom(String oldLocalValue)
    {

    }

    @Override
    public Option<String> getUsageHint()
    {
        return Option.option(numberField.getEmptyText());
    }

    @Override
    public void setUsageHint(String usageHint)
    {
        numberField.setEmptyText(usageHint);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> numberValueChangeHandler)
    {
        return timeErrorHandler.addValueChangeHandler(numberValueChangeHandler);
    }
}
