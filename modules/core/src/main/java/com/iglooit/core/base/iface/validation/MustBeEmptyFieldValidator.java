package com.iglooit.core.base.iface.validation;

import com.clarity.commons.iface.domain.meta.Meta;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.domain.Validator;
import com.clarity.commons.iface.util.StringUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class MustBeEmptyFieldValidator extends Validator
{

    private String errorMsg;

    public MustBeEmptyFieldValidator(String tag, String validationFailMsg)
    {
        super(tag);
        this.errorMsg = validationFailMsg;
    }

    @Override
    public List<ValidationResult> validate(Meta instance)
    {
        for (String tag : getTags())
        {
            Object value = instance.get(tag);

            if (value != null && !StringUtil.isBlank(value.toString()))
            {
                return Arrays.asList(new ValidationResult(getTags(), errorMsg));
            }
        }
        return Collections.emptyList();
    }
}
