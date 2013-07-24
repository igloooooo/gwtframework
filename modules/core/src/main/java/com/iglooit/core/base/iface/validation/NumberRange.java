package com.iglooit.core.base.iface.validation;

import com.iglooit.commons.iface.domain.I18NFactoryProvider;
import com.iglooit.commons.iface.domain.ValidationResult;
import com.iglooit.commons.iface.domain.Validator;
import com.iglooit.commons.iface.domain.meta.Meta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NumberRange extends Validator
{
    private final double maxValue;
    private final double minValue;
    private String propertyName;
    private static final ValidationConstants VC = I18NFactoryProvider.get(ValidationConstants.class);

    public NumberRange(double minValue, double maxValue, String propertyName)
    {
        super(propertyName);
        this.maxValue = maxValue;
        this.minValue = minValue;
        this.propertyName = propertyName;
    }

    public List<ValidationResult> validate(Meta instance)
    {
        String propertyValue = instance.get(propertyName);
        if (propertyValue == null || propertyValue.equals(""))
            return Collections.emptyList();
        if (Double.parseDouble(propertyValue) > maxValue || Double.parseDouble(propertyValue) < minValue)
            return Arrays.asList(
                new ValidationResult(getTags(), VC.outOfValidNumberRange(minValue, maxValue)));
        return Collections.emptyList();
    }

    public double getMaxValue()
    {
        return maxValue;
    }

    public double getMinValue()
    {
        return minValue;
    }
}
