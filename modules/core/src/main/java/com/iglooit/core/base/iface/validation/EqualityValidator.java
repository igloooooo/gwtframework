package com.iglooit.core.base.iface.validation;

import com.clarity.commons.iface.domain.meta.Meta;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.domain.Validator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class EqualityValidator extends Validator
{
    private String tag;
    private Object value;
    private String errorMsg;

    public EqualityValidator(String tag, Object value, String errorMsg)
    {
        super(tag);
        this.tag = tag;
        this.value = value;
        this.errorMsg = errorMsg;
    }

    @Override
    public List<ValidationResult> validate(Meta instance)
    {
        if (value != null && value.equals(instance.get(tag)))
        {
            return Collections.emptyList();
        }
        else
            return Arrays.asList(new ValidationResult(getTags(), errorMsg));
    }
}
