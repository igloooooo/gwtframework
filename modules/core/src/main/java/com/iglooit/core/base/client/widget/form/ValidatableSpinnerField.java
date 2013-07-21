package com.iglooit.core.base.client.widget.form;

import com.clarity.core.base.client.mvp.ClarityField;
import com.clarity.core.base.client.mvp.DelayedTimeValidationResultHandler;
import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.type.Option;
import com.extjs.gxt.ui.client.widget.form.SpinnerField;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Timer;

import java.util.Arrays;
import java.util.List;

public class ValidatableSpinnerField implements ClarityField<String, SpinnerField>
{
    private SpinnerField spinnerField;
    private double defaultValue = 0.0;

    private final DelayedTimeValidationResultHandler<String> timeErrorHandler;

    public ValidatableSpinnerField()
    {
        spinnerField = new SpinnerField();
        spinnerField.getImages().setInvalid(Resource.ICONS.exclamationRed());
        getField().setFireChangeEventOnSetValue(false);
        timeErrorHandler = new DelayedTimeValidationResultHandler<String>(this, new Runnable()
        {
            public void run()
            {
                fireValidationChangeEvent();
            }
        });

    }

    public ValidatableSpinnerField(double defaultValue)
    {
        this.defaultValue = defaultValue;
        spinnerField = new SpinnerField();
        spinnerField.setValue(defaultValue);
        spinnerField.getImages().setInvalid(Resource.ICONS.exclamationRed());
        getField().setFireChangeEventOnSetValue(false);
        timeErrorHandler = new DelayedTimeValidationResultHandler<String>(this, new Runnable()
        {
            public void run()
            {
                fireValidationChangeEvent();
            }
        });
    }

    public static final int VALIDATE_DELAY_MILLIS = 500;

    private boolean typingTimeout = false;
    private String validationShowErrorsMessage = "";

    private final Timer validationShowErrorsTimer = new Timer()
    {
        public void run()
        {
            spinnerField.forceInvalid(validationShowErrorsMessage);
        }
    };

    private boolean fireChangeEvents = true;

    @Override
    public SpinnerField getField()
    {
        return spinnerField;
    }


    @Override
    public void handleValidationResults(List<ValidationResult> validationResultList)
    {
        timeErrorHandler.handleValidationResults(validationResultList);
    }

    private static final List<Integer> SPECIAL_CHARS = Arrays.asList(
        KeyCodes.KEY_ALT,
        //KeyCodes.KEY_BACKSPACE,
        KeyCodes.KEY_CTRL,
        //KeyCodes.KEY_DELETE,
        KeyCodes.KEY_DOWN,
        KeyCodes.KEY_END,
        KeyCodes.KEY_ENTER,
        KeyCodes.KEY_ESCAPE,
        KeyCodes.KEY_HOME,
        KeyCodes.KEY_LEFT,
        KeyCodes.KEY_PAGEDOWN,
        KeyCodes.KEY_PAGEUP,
        KeyCodes.KEY_RIGHT,
        KeyCodes.KEY_SHIFT,
        KeyCodes.KEY_TAB,
        KeyCodes.KEY_UP
    );

    private static boolean isSpecialChar(int c)
    {
        return SPECIAL_CHARS.contains(Integer.valueOf(c));
    }

    @Override
    public void valueExternallyChangedFrom(String oldLocalValue)
    {
    }


    @Override
    public String getFieldLabel()
    {
        return spinnerField.getFieldLabel();
    }

    @Override
    public void setFieldLabel(String fieldLabel)
    {
        spinnerField.setFieldLabel(fieldLabel);
    }


    @Override
    public Option<String> getUsageHint()
    {
        return Option.option(spinnerField.getEmptyText());
    }

    @Override
    public void setUsageHint(String usageHint)
    {
        spinnerField.setEmptyText(usageHint);
    }

    public String getValue()
    {
        Number superValue = spinnerField.getValue();
        if (superValue == null)
            return "";
        return superValue.toString();
    }

    @Override
    public void setValue(String s)
    {
        if (s == null)
            spinnerField.setValue(defaultValue);
        else
        {
            Number number = Double.valueOf(s);
            spinnerField.setValue(number);
        }
    }

    @Override
    public void setValue(String s, boolean b)
    {
        fireChangeEvents = b;
        setValue(s);
        fireValidationChangeEvent();
        fireChangeEvents = true;
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> sValueChangeHandler)
    {
        return timeErrorHandler.addValueChangeHandler(sValueChangeHandler);
    }

    private void fireValidationChangeEvent()
    {
        if (fireChangeEvents)
        {
            String value = getValue();
            ValueChangeEvent.fire(ValidatableSpinnerField.this, value);
        }
    }

    public void fireEvent(GwtEvent<?> event)
    {
        getField().fireEvent(event);
        timeErrorHandler.fireEvent(event);
    }

}
