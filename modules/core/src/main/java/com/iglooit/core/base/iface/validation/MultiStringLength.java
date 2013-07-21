package com.iglooit.core.base.iface.validation;

import com.clarity.commons.iface.domain.meta.Meta;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.domain.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class MultiStringLength extends Validator
{
    private final int totalStrlen;

    public MultiStringLength(int totalStrlen, String... propertyNames)
    {
        super(propertyNames);
        this.totalStrlen = totalStrlen;
    }

    public List<ValidationResult> validate(Meta instance)
    {
        List<ValidationResult> results = new ArrayList<ValidationResult>();
        int totalLen = 0;
        for (String propertyName : getTags())
        {
            String propertyValue = instance.get(propertyName);
            if (propertyValue == null)
                return Collections.emptyList();
            totalLen += propertyValue.length();
        }
        if (totalLen > totalStrlen)
            results.add(new ValidationResult(getTags(), "total string length exceeds " + totalStrlen));
        return results;
    }
}
