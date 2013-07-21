package com.iglooit.core.base.iface.validation;

import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.domain.Validator;
import com.clarity.commons.iface.domain.meta.Meta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: ftsang Date: 9/07/12 Time: 5:27 PM
 */
public class NumericNumberRange extends Validator
{
    private static final ValidationConstants VC = I18NFactoryProvider.get(ValidationConstants.class);
    private long min;
    private long max;
    private String propertyName;
    private boolean allowNull = false;

    public NumericNumberRange(long min, long max, String propertyName)
    {
        this(min, max, false, propertyName);
    }

    public NumericNumberRange(long min, long max, boolean allowNull, String propertyName)
    {
        super(propertyName);
        this.min = min;
        this.max = max;
        this.allowNull = allowNull;
        this.propertyName = propertyName;
    }

    public boolean isAllowNull()
    {
        return allowNull;
    }

    public void setAllowNull(boolean allowNull)
    {
        this.allowNull = allowNull;
    }

    @Override
    public List<ValidationResult> validate(Meta instance)
    {
        Number nVal = (Number)instance.get(propertyName);
        Long val = nVal == null ? null : nVal.longValue();

        if (val == null && allowNull)
            return Collections.emptyList();
        if (val == null || val.compareTo(min) < 0 || val.compareTo(max) > 0)
            return Arrays.asList(new ValidationResult(getTags(), VC.outOfValidNumberRange((double)min, (double)max)));
        else
            return Collections.emptyList();
    }
}
