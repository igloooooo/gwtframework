package com.iglooit.core.base.iface.validation;

import com.iglooit.commons.iface.domain.I18NFactoryProvider;
import com.iglooit.commons.iface.domain.ValidationResult;
import com.iglooit.commons.iface.domain.Validator;
import com.iglooit.commons.iface.domain.meta.Meta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Validator for where one, and only one, of a list of fields must be valid according to the passed in validators
 */
public class XORDelegatingValidator extends Validator
{
    private static final ValidationConstants VALIDATION_CONSTANTS = I18NFactoryProvider.get(ValidationConstants.class);
    private Collection<Validator> validators;


    public XORDelegatingValidator(Collection<String> tags, Collection<Validator> validators)
    {
        super(tags);
        this.validators = validators;
    }

    public List<ValidationResult> validate(Meta instance)
    {
        List<ValidationResult> allEmbeddedValidationResults = new ArrayList<ValidationResult>();

        for (Validator validator : validators)
        {
            List<ValidationResult> individualEmbeddedValidationResult = validator.validate(instance);
            if (!individualEmbeddedValidationResult.isEmpty() && !allEmbeddedValidationResults.isEmpty())
            {
                //two pieces have failed validation, only one can pass, fail validation
                return getFailureList(instance);
            }
            allEmbeddedValidationResults.addAll(individualEmbeddedValidationResult);
        }

        //if all validators pass, fail validation as only one can (XOR)
        if (allEmbeddedValidationResults.isEmpty())
        {
            return getFailureList(instance);
        }
        else
        {
            return Collections.emptyList();
        }
    }

    private List<ValidationResult> getFailureList(Meta instance)
    {
        //note that any embedded validation errors encountered are discarded and replaced with an XOR one.
        return Arrays.asList(new ValidationResult(getTags(), getErrorString(instance)));
    }

    public String getErrorString(Meta instance)
    {
        return VALIDATION_CONSTANTS.onlyOneFieldMustPass();
    }

}
