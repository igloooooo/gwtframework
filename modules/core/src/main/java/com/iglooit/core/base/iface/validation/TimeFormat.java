package com.iglooit.core.base.iface.validation;

import com.clarity.core.base.client.widget.form.ClarityTimeField;
import com.clarity.commons.iface.domain.meta.Meta;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.domain.Validator;
import com.google.gwt.i18n.client.DateTimeFormat;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Time of day
 */
public class TimeFormat extends Validator
{
    private String propertyName;

    public TimeFormat(String propertyName)
    {
        super(propertyName);
        this.propertyName = propertyName;
    }

    public List<ValidationResult> validate(Meta instance)
    {
        String propertyValue = instance.get(propertyName);
        if (propertyValue == null)
            return Collections.emptyList();
        Date tryDate;
        try
        {
            tryDate = DateTimeFormat.getFormat(ClarityTimeField.DISPLAY_TIME_FORMAT).parse(propertyValue);
        }
        catch (Exception e)
        {
            try
            {
                tryDate = DateTimeFormat.getFormat(ClarityTimeField.COMMUNICATE_TIME_FORMAT).parse(propertyValue);
            }
            catch (Exception e2)
            {
                return Arrays.asList(new ValidationResult(getTags(), "Invalid time format"));
            }

        }
        if (tryDate == null)
            return Arrays.asList(new ValidationResult(getTags(), "Invalid time format"));
        return Collections.emptyList();
    }
}
