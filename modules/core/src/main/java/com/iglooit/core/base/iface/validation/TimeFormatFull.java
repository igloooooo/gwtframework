package com.iglooit.core.base.iface.validation;

import com.clarity.commons.iface.domain.meta.Meta;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.domain.Validator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Span of Time (eg: 99 hours, 59 mins). For time of day
 * @see TimeFormat
 */
public class TimeFormatFull extends Validator
{
    private String propertyName;

    public TimeFormatFull(String propertyName)
    {
        super(propertyName);
        this.propertyName = propertyName;
    }

    public List<ValidationResult> validate(Meta instance)
    {
        String propertyValue = instance.get(propertyName);
        if (propertyValue == null)
            return Collections.emptyList();
        if (!propertyValue.matches("[0-9][0-9]:[0-5][0-9]"))
            return Arrays.asList(
                new ValidationResult(getTags(), "must follow time format(hh:mm)"));
        return Collections.emptyList();
    }
}
