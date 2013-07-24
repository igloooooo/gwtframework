package com.iglooit.core.base.iface.validation;

import com.iglooit.commons.iface.domain.I18NFactoryProvider;
import com.iglooit.commons.iface.domain.ValidationResult;
import com.iglooit.commons.iface.domain.Validator;
import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.util.StringUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class RequiredField extends Validator
{
    private String propertyName;
    private static final ValidationConstants VC = I18NFactoryProvider.get(ValidationConstants.class);

    public RequiredField(String propertyName)
    {
        super(propertyName);
        this.propertyName = propertyName;
    }

    public List<ValidationResult> validate(Meta instance)
    {
        Object propertyValue = instance.get(propertyName);
        if (propertyValue == null || StringUtil.isBlank(propertyValue.toString()))
            return Arrays.asList(new ValidationResult(getTags(), VC.fieldRequired()));
        return Collections.emptyList();
    }
}
