package com.iglooit.core.base.iface.validation;

import com.iglooit.commons.iface.domain.I18NFactoryProvider;
import com.iglooit.commons.iface.domain.ValidationResult;
import com.iglooit.commons.iface.domain.Validator;
import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.AppX;
import com.iglooit.commons.iface.type.Option;
import com.iglooit.core.base.iface.domain.DomainEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: ftsang Date: 13/07/12 Time: 10:01 AM
 */
public class DateComparisonValidator extends Validator
{
    private String propertyNameDateFirst;
    private String propertyNameDateSecond;

    private ComparisonOperator operator;
    private static final ValidationConstants VC = I18NFactoryProvider.get(ValidationConstants.class);
    private String firstDateLabel;
    private String secondDateLabel;

    public enum ComparisonOperator
    {
        GT
            {
                @Override
                public boolean compare(Date first, Date second)
                {
                    return second.after(first);
                }
            }, // greater than
        GE
            {
                @Override
                public boolean compare(Date first, Date second)
                {
                    return !second.before(first);
                }
            }, // greater than equal
        LT
            {
                @Override
                public boolean compare(Date first, Date second)
                {
                    return first.before(second);
                }
            }, // less than
        LE
            {
                @Override
                public boolean compare(Date first, Date second)
                {
                    return !first.after(second);
                }
            }, // less than equal
        EQ
            {
                @Override
                public boolean compare(Date first, Date second)
                {
                    return first.equals(second);
                }
            }, // equal
        NE
            {
                @Override
                public boolean compare(Date first, Date second)
                {
                    return !first.equals(second);
                }
            };

        public abstract boolean compare(Date first, Date second);

        public String toString()
        {
            switch (this)
            {
                case GT:
                    return VC.greaterThan();
                case GE:
                    return VC.greaterThanEqual();
                case LT:
                    return VC.lessThan();
                case LE:
                    return VC.lessThanEqual();
                case EQ:
                    return VC.equalTo();
                case NE:
                    return VC.notEqualTo();
                default:
                    throw new AppX("Unhandled date comparison operator");
            }
        }
    }

    public DateComparisonValidator(String propertyNameDateFirst, String propertyNameDateSecond,
                                   ComparisonOperator operator)
    {
        super(propertyNameDateFirst, propertyNameDateSecond);
        this.propertyNameDateFirst = propertyNameDateFirst;
        this.propertyNameDateSecond = propertyNameDateSecond;
        this.operator = operator;
    }

    public String getFirstDateLabel()
    {
        return firstDateLabel;
    }

    public void setFirstDateLabel(String firstDateLabel)
    {
        this.firstDateLabel = firstDateLabel;
    }

    public String getSecondDateLabel()
    {
        return secondDateLabel;
    }

    public void setSecondDateLabel(String secondDateLabel)
    {
        this.secondDateLabel = secondDateLabel;
    }

    @Override
    public List<ValidationResult> validate(Meta instance)
    {
        Date firstDate = (Date)instance.get(propertyNameDateFirst);
        if (firstDate == null)
            return Collections.emptyList();
        Date secondDate = (Date)instance.get(propertyNameDateSecond);
        if (secondDate == null)
            return Collections.emptyList();

        boolean result = operator.compare(firstDate, secondDate);

        if (result)
        {
            ((DomainEntity)instance).notifyValidationHandlers(Collections.<ValidationResult>emptyList(),
                Option.some(propertyNameDateFirst));
            ((DomainEntity)instance).notifyValidationHandlers(Collections.<ValidationResult>emptyList(),
                Option.some(propertyNameDateSecond));
            return Collections.<ValidationResult>emptyList();
        }
        else
        {
            String message = VC.invalidDateComparison(
                firstDateLabel == null ? VC.firstDateLabel() : firstDateLabel,
                operator.toString(),
                secondDateLabel == null ? VC.secondDateLabel() : secondDateLabel
            );

            if (instance instanceof DomainEntity)
            {
                DomainEntity de = (DomainEntity)instance;
                if (firstDateLabel == null && secondDateLabel == null)
                {
                    firstDateLabel = de.getDefaultFieldLabel(propertyNameDateFirst);
                    secondDateLabel = de.getDefaultFieldLabel(propertyNameDateSecond);
                }

                message = VC.invalidDateComparison(firstDateLabel, operator.toString(), secondDateLabel);

                ValidationResult vrFirst = new ValidationResult(Arrays.asList(propertyNameDateFirst), message);
                ValidationResult vrSecond = new ValidationResult(Arrays.asList(propertyNameDateSecond), message);
                de.notifyValidationHandlers(Arrays.asList(vrFirst),
                    Option.some(propertyNameDateFirst));
                de.notifyValidationHandlers(Arrays.asList(vrSecond),
                    Option.some(propertyNameDateSecond));

                return Arrays.asList(new ValidationResult(getTags(), message));
            }

            return Arrays.asList(new ValidationResult(getTags(), message));
        }
    }
}
