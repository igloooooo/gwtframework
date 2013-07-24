package com.iglooit.core.base.iface.validation;

import com.iglooit.commons.iface.domain.ValidationResult;
import com.iglooit.commons.iface.domain.Validator;
import com.iglooit.commons.iface.domain.meta.Meta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class PhoneNumberFormat extends Validator
{
    private String propertyName;

    public PhoneNumberFormat(String propertyName)
    {
        super(propertyName);
        this.propertyName = propertyName;
    }

    public List<ValidationResult> validate(Meta instance)
    {
        String propertyValue = instance.get(propertyName);
        if (propertyValue == null)
            return Collections.emptyList();
        if (!propertyValue.matches("[\\+]?[0-9\\(\\)\\s-]*[0-9][0-9\\(\\)\\s-]*"))
            return Arrays.asList(
                new ValidationResult(getTags(), "must follow phone format"));
        return Collections.emptyList();
    }
}
