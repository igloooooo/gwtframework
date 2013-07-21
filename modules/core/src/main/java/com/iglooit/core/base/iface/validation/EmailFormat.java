package com.iglooit.core.base.iface.validation;

import com.clarity.commons.iface.domain.meta.Meta;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.domain.Validator;
import com.clarity.commons.iface.domain.I18NFactoryProvider;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EmailFormat extends Validator
{
    private static final ValidationConstants VC = I18NFactoryProvider.get(ValidationConstants.class);

    private String propertyName;

    public EmailFormat(String propertyName)
    {
        super(propertyName);
        this.propertyName = propertyName;
    }

    public List<ValidationResult> validate(Meta instance)
    {
        String propertyValue = instance.get(propertyName);
        if (propertyValue == null)
            return Collections.emptyList();
        if (!propertyValue.matches("^[\\w\\.\\+\\-]+@[\\w\\.\\+\\-]+\\.[a-zA-Z]{2,4}$"))
            return Arrays.asList(new ValidationResult(getTags(), VC.emailFormatFail()));

//        if (!propertyValue.matches("[a-zA-Z0-9_-]+[\\@][a-zA-Z0-9]+[\\.]com$"))
//            return Arrays.asList(
//                new ValidationResult(getTags(), "must follow format: ***@***.com"));
        return Collections.emptyList();
    }
}
