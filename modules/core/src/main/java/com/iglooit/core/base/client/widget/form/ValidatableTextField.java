package com.iglooit.core.base.client.widget.form;

import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.util.StringUtil;
import com.clarity.core.base.iface.validation.ValidationConstants;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;

import java.util.Arrays;

public class ValidatableTextField extends ValidatableTextFieldBase<String>
{

    private static final ValidationConstants VC = I18NFactoryProvider.get(ValidationConstants.class);

    private  boolean emptyCheck = false;

    public interface ValidatableTextChangeHandler
    {
        void onChange();
    }

    public ValidatableTextField()
    {
        super();
        this.addValueChangeHandler(new ValueChangeHandler<String>()
        {
            @Override
            public void onValueChange(ValueChangeEvent<String> valueChangeEvent)
            {
               if (emptyCheck)
                   validate();
            }
        });
    }

    public ValidatableTextField(final ValidatableTextChangeHandler change)
    {
        super();
        this.addValueChangeHandler(new ValueChangeHandler<String>()
        {
            @Override
            public void onValueChange(ValueChangeEvent<String> valueChangeEvent)
            {
                if (emptyCheck)
                {
                    validate();
                }
                change.onChange();

            }
        });
    }


    protected String convertType(String typedValue)
    {
        return typedValue;
    }

    @Override
    public boolean validate()
    {
        if (emptyCheck)
            return validate(false);
        else
            return super.validate();
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

    public void forceEmpty()
    {
        setValue(null);
        hasFocus = false;
        applyEmptyText();
    }
    
}
