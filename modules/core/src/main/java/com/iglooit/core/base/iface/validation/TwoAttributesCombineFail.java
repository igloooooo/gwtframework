package com.iglooit.core.base.iface.validation;

import com.iglooit.commons.iface.domain.I18NFactoryProvider;
import com.iglooit.commons.iface.domain.ValidationResult;
import com.iglooit.commons.iface.domain.Validator;
import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.AppX;
import com.iglooit.commons.iface.type.Option;
import com.iglooit.core.base.iface.domain.DomainEntity;
import com.iglooit.core.base.iface.domain.ValidatableMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class TwoAttributesCombineFail extends Validator
{
    private boolean notifier;

    private String firstProperty;
    private String secondProperty;

    private Object firstValue;
    private Object secondValue;

    private String message;

    private ValidatableMeta meta;

    private static final ValidationConstants VC = I18NFactoryProvider.get(ValidationConstants.class);

    public TwoAttributesCombineFail(String firstProperty, String secondProperty,
                                    Object firstValue, Object secondValue,
                                    String message)
    {
        super(firstProperty);
        this.firstProperty = firstProperty;
        this.secondProperty = secondProperty;
        this.firstValue = firstValue;
        this.secondValue = secondValue;
        this.message = message;
        this.notifier = false;
    }

    public boolean isNotifier()
    {
        return notifier;
    }

    public void setNotifier(boolean notifier)
    {
        this.notifier = notifier;
    }

    @Override
    public List<ValidationResult> validate(Meta instance)
    {
        if (!(instance instanceof ValidatableMeta))
        {
            throw new AppX("The instance should be validatable meta");
        }
        else
        {
            this.meta = (ValidatableMeta)instance;
        }

        Object firstActualValue = instance.get(firstProperty);
        Object secondActualValue = instance.get(secondProperty);


        boolean firstValueEquals = firstActualValue != null && firstActualValue.equals(firstValue);
        boolean secondValueEquals = secondActualValue != null && secondActualValue.equals(secondValue);
        if (firstValueEquals && secondValueEquals)
        {
            boolean theOtherNotifier = getSecondNotifier();
            if (!theOtherNotifier)
            {
                notifier = true;
                if (instance instanceof DomainEntity)
                {
                    ((DomainEntity)instance).validateAndNotify(Option.some(secondProperty),
                        ((DomainEntity)instance).getValidators(Option.some(secondProperty))
                    );
                }
            }
            else
            {
                setSecondNotifier(false);
            }
            return Arrays.asList(new ValidationResult(getTags(), message));
        }
        else
        {
            boolean theOtherNotifier = getSecondNotifier();
            if (!theOtherNotifier)
            {
                notifier = false;
                if (instance instanceof DomainEntity)
                {
                    ((DomainEntity)instance).validateAndNotify(Option.some(secondProperty),
                        validatorListOtherThanCurrentValidator(
                            ((DomainEntity)instance).getValidators(Option.some(secondProperty))
                        ));
                }
            }
            return Collections.emptyList();
        }
    }

    private void setSecondNotifier(boolean b)
    {
        if (getValidatorFromSecondAttribute() != null)
            getValidatorFromSecondAttribute().setNotifier(b);
    }

    private boolean getSecondNotifier()
    {
        if (getValidatorFromSecondAttribute() != null)
            return getValidatorFromSecondAttribute().isNotifier();
        return false;
    }

    private TwoAttributesCombineFail getValidatorFromSecondAttribute()
    {
        List<Validator> secondPropertyValidatorList = meta.getValidators(Option.some(secondProperty));
        if (secondPropertyValidatorList == null)
            return null;
        for (Validator validator : secondPropertyValidatorList)
        {
            if (validator instanceof TwoAttributesCombineFail)
            {
                return (TwoAttributesCombineFail)validator;
            }
        }
        return null;
    }

    private List<Validator> validatorListOtherThanCurrentValidator(List<Validator> validators)
    {
        List<Validator> newValidators = new ArrayList<Validator>(validators);
        Iterator<Validator> iterator = newValidators.iterator();
        while (iterator.hasNext())
        {
            Validator validator = iterator.next();
            if (validator instanceof TwoAttributesCombineFail)
            {
                iterator.remove();
            }
        }
        return newValidators;
    }
}
