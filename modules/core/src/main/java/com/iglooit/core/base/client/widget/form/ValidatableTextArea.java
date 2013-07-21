package com.iglooit.core.base.client.widget.form;

import com.clarity.core.base.client.mvp.ClarityField;
import com.clarity.core.base.client.mvp.DelayedTimeValidationResultHandler;
import com.clarity.core.base.client.view.resource.Resource;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.core.base.iface.validation.ValidationConstants;
import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.commons.iface.type.Option;
import com.clarity.commons.iface.util.StringUtil;
import com.extjs.gxt.ui.client.widget.form.TextArea;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;

import java.util.Arrays;
import java.util.List;

public class ValidatableTextArea extends TextArea
    implements ClarityField<String, ValidatableTextArea>
{
    private static final ValidationConstants VC = I18NFactoryProvider.get(ValidationConstants.class);
    
    private boolean fireChangeEvents = true;
    private final DelayedTimeValidationResultHandler<String> timeErrorHandler;

    private boolean emptyCheck = false;

    public ValidatableTextArea()
    {
        super();
        getImages().setInvalid(Resource.ICONS.exclamationRed());
        setFireChangeEventOnSetValue(false);
        timeErrorHandler = new DelayedTimeValidationResultHandler<String>(this, new Runnable()
        {
            public void run()
            {
                fireValidationChangeEvent();
            }
        });
    }

    protected void fireValidationChangeEvent()
    {
        if (fireChangeEvents)
        {
            String value = getValue();
            ValueChangeEvent.fire(ValidatableTextArea.this, value);
        }
    }

    public ValidatableTextArea getField()
    {
        return this;
    }

    public void handleValidationResults(List<ValidationResult> validationResultList)
    {
        timeErrorHandler.handleValidationResults(validationResultList);
    }

    public void valueExternallyChangedFrom(String oldLocalValue)
    {

    }

    public Option<String> getUsageHint()
    {
        return Option.option(getEmptyText());
    }

    public void setUsageHint(String usageHint)
    {
        setEmptyText(usageHint);
    }

    public void setValue(String d)
    {
        setValue(d, true);
    }
//
//    public String getValue()
//    {
//        return super.getValue();
//    }

    public void setValue(String s, boolean b)
    {
        fireChangeEvents = b;
        super.setValue(s);
        fireValidationChangeEvent();
        fireChangeEvents = true;
    }

    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<String> stringValueChangeHandler)
    {
        return timeErrorHandler.addValueChangeHandler(stringValueChangeHandler);
    }

    @Override
    public void fireEvent(GwtEvent<?> event)
    {
        super.fireEvent(event);
        timeErrorHandler.fireEvent(event);
    }

    @Override
    protected boolean validateValue(String value)
    {
        if (emptyCheck)
        {
            boolean valid = false;
            valid =  !StringUtil.isEmpty(value);
            if (!valid)
                getField().handleValidationResults(Arrays.asList(new ValidationResult(null, VC.fieldRequired())));
            return valid;
        }
        return super.validateValue(value);
    }

    public void setEmptyCheck(boolean emptyCheck)
    {
        this.emptyCheck = emptyCheck;
    }
}
