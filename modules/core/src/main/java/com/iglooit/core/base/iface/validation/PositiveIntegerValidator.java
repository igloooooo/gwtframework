package com.iglooit.core.base.iface.validation;

import com.iglooit.commons.iface.domain.I18NFactoryProvider;
import com.iglooit.commons.iface.domain.ValidationResult;
import com.iglooit.commons.iface.domain.Validator;
import com.iglooit.commons.iface.domain.meta.Meta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PositiveIntegerValidator extends Validator
{
    private String propertyName;
    private static final ValidationConstants VC = I18NFactoryProvider.get(ValidationConstants.class);

    public PositiveIntegerValidator(String propertyName)
    {
        super(propertyName);
        this.propertyName = propertyName;
    }

    @Override
    public List<ValidationResult> validate(Meta instance)
    {
        //To support this validator to be used by fields with types other than String like Long, Number
        String propertyValue = String.valueOf(instance.get(propertyName));
        if (propertyValue == null || propertyValue.equalsIgnoreCase("null"))
            return Collections.emptyList();
        Double value;
        try
        {
            value = Double.valueOf(propertyValue);
        }
        catch (NumberFormatException e)
        {
            return Arrays.asList(
                new ValidationResult(getTags(), VC.invalidNumber()));
        }
        if (value != Math.round(value))
            return Arrays.asList(new ValidationResult(getTags(), VC.invalidInteger()));
        if (value <= 0)
            return Arrays.asList(new ValidationResult(getTags(), VC.positiveIntegerFail()));


        return Collections.emptyList();
    }
}
