package com.iglooit.core.base.iface.validation;

import com.clarity.commons.iface.domain.meta.Meta;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.domain.Validator;

import java.util.Collections;
import java.util.List;

/**
 * This validator represents logical implication: A implies B
 */
public class DependentFieldDelegatingValidator extends Validator
{

    private Validator dependentValidator;
    private Validator dependeeValidator;

    /**
     * @param dependeeValidator if this validates, dependentValidator will be run, if not, true is returned.
     * @param dependentValidator run if dependeeValidator passes, results of this returned as validated
     */
    public DependentFieldDelegatingValidator(Validator dependeeValidator, Validator dependentValidator)
    {
        super(getTagsListForValidators(dependeeValidator, dependentValidator));
        this.dependeeValidator = dependeeValidator;
        this.dependentValidator = dependentValidator;
    }

    @Override
    public List<ValidationResult> validate(Meta instance)
    {
        if (dependeeValidator.validate(instance).isEmpty())
        {
            return dependentValidator.validate(instance);
        }
        return Collections.emptyList();
    }


}
