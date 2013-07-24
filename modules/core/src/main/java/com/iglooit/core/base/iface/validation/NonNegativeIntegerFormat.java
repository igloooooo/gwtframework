package com.iglooit.core.base.iface.validation;

import com.iglooit.commons.iface.domain.ValidationResult;
import com.iglooit.commons.iface.domain.Validator;
import com.iglooit.commons.iface.domain.meta.Meta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NonNegativeIntegerFormat extends Validator
{
    private String propertyName;

    public NonNegativeIntegerFormat(String propertyName)
    {
        super(propertyName);
        this.propertyName = propertyName;
    }

    public List<ValidationResult> validate(Meta instance)
    {
        //To support this validator to be used by fields with types other than String like Long, Number
        String propertyValue = String.valueOf(instance.get(propertyName));
        if (propertyValue == null || propertyValue.equalsIgnoreCase("null"))
            return Collections.emptyList();
        try
        {
            Integer.parseInt(propertyValue);
        }
        catch (NumberFormatException e)
        {
            return Arrays.asList(
                new ValidationResult(getTags(), "must be numbers"));
        }
        if (Integer.parseInt(propertyValue) < 0)
            return Arrays.asList(new ValidationResult(getTags(), "must be larger than 0 and " +
                "no more than " + Integer.MAX_VALUE));


        return Collections.emptyList();
    }
}
