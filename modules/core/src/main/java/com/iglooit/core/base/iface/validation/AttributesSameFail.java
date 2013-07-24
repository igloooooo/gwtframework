package com.iglooit.core.base.iface.validation;

import com.iglooit.commons.iface.domain.ValidationResult;
import com.iglooit.commons.iface.domain.Validator;
import com.iglooit.commons.iface.domain.meta.Meta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class AttributesSameFail extends Validator
{
    private String attribute1;
    private String attribute2;
    private String message;

    public AttributesSameFail(String attribute1, String attribute2, String message)
    {
        super(Arrays.asList(attribute1));
        this.attribute1 = attribute1;
        this.attribute2 = attribute2;
        this.message = message;
    }

    @Override
    public List<ValidationResult> validate(Meta meta)
    {
        if (notNullEquals(meta.get(attribute1), meta.get(attribute2)))
            return Arrays.asList(new ValidationResult(getTags(), message));
        return Collections.emptyList();
    }

    public boolean notNullEquals(Object object1, Object object2)
    {
        if (object1 == null || object2 == null)
            return false;
        return object1.equals(object2);
    }
}
