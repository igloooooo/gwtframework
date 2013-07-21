package com.iglooit.core.base.iface.validation;

import com.clarity.commons.iface.domain.meta.Meta;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.domain.Validator;
import com.clarity.commons.iface.domain.I18NFactoryProvider;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MaxStringLength extends Validator
{
    private final int maxLength;
    private String propertyName;
    private static final ValidationConstants VC = I18NFactoryProvider.get(ValidationConstants.class);

    public MaxStringLength(int maxLength, String propertyName)
    {
        super(propertyName);
        this.propertyName = propertyName;
        this.maxLength = maxLength;
    }

    public List<ValidationResult> validate(Meta instance)
    {
        String propertyValue = instance.get(propertyName);
        if (propertyValue == null)
            return Collections.emptyList();
        if (propertyValue.length() > maxLength)
            return Arrays.asList(
                new ValidationResult(getTags(), VC.maxStringLengthFail(maxLength)));
        return Collections.emptyList();
    }

    public int getMaxLength()
    {
        return maxLength;
    }
}
