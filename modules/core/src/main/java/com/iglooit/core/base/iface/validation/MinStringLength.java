package com.iglooit.core.base.iface.validation;

import com.iglooit.commons.iface.domain.I18NFactoryProvider;
import com.iglooit.commons.iface.domain.ValidationResult;
import com.iglooit.commons.iface.domain.Validator;
import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.util.StringUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MinStringLength extends Validator
{
    private final int minLength;
    private String propertyName;
    private static final ValidationConstants VC = I18NFactoryProvider.get(ValidationConstants.class);

    public MinStringLength(int minLength, String propertyName)
    {
        super(propertyName);
        this.propertyName = propertyName;
        this.minLength = minLength;
    }

    public List<ValidationResult> validate(Meta instance)
    {
        String propertyValue = StringUtil.emptyStringIfNull((String)instance.get(propertyName));
        if (propertyValue.length() < minLength)
            return Arrays.asList(new ValidationResult(getTags(), VC.minStringLengthFail(minLength)));
        return Collections.emptyList();
    }

    public int getMinLength()
    {
        return minLength;
    }
}
