package com.iglooit.core.base.iface.validation;

import com.clarity.commons.iface.domain.meta.Meta;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.domain.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Complexity extends Validator
{
    private String propertyName;

    public Complexity(String propertyName)
    {
        super(propertyName);
        this.propertyName = propertyName;
    }

    public List<ValidationResult> validate(Meta instance)
    {
        String propertyValue = instance.get(propertyName);
        if (propertyValue == null)
            return Collections.emptyList();
        ValidationResult noDigit = new ValidationResult(getTags(), "must contain at least 1 digit");
        ValidationResult noChar = new ValidationResult(getTags(), "must contain at least 1 char");
        ValidationResult noPunc = new ValidationResult(getTags(), "must contain at least 1 punctuation");
        ValidationResult ws = new ValidationResult(getTags(), "must not contain whitespace");

        List<ValidationResult> retList = new ArrayList<ValidationResult>();

        if (!propertyValue.matches(".*[0-9].*"))
            retList.add(noDigit);
        if (!propertyValue.matches(".*[a-zA-Z].*"))
            retList.add(noChar);
        if (propertyValue.matches(".*\\s.*"))
            retList.add(ws);
        if (!propertyValue.matches(".*[\\W_].*"))
            retList.add(noPunc);
        return retList;
    }
}
