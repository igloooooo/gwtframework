package com.iglooit.core.base.iface.domain;

import com.clarity.commons.iface.annotation.NoMetaAccess;
import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.domain.Validator;
import com.clarity.commons.iface.domain.meta.Meta;
import com.clarity.commons.iface.type.AppX;
import com.clarity.core.base.client.view.BaseViewConstants;
import com.clarity.core.base.iface.validation.DateComparisonValidator;
import com.clarity.core.base.iface.validation.DependentFieldDelegatingValidator;
import com.clarity.core.base.iface.validation.EqualityValidator;
import com.clarity.core.base.iface.validation.NumericNumberRange;
import com.clarity.commons.iface.util.TimeUtil;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ftsang
 * Date: 22/06/12
 * Time: 12:27 PM
 */
public class DateTimeRangeInfo extends DomainEntity
{
    public static class Holder extends MetaBasedDomainEntity
    {
        public static final String VALUE_PROPERTY = "value";

        public Holder(DateTimeRangeInfo dtri)
        {
            super();
            set(VALUE_PROPERTY, dtri);
        }

        public DateTimeRangeInfo getValue()
        {
            return get(VALUE_PROPERTY);
        }

        public void setValue(DateTimeRangeInfo dtri)
        {
            set(VALUE_PROPERTY, dtri);
        }

        @Override
        public List<Validator> doGetValidators()
        {
            return Collections.emptyList();
        }
    }

    private static final BaseViewConstants BVC = I18NFactoryProvider.get(BaseViewConstants.class);

    public enum RelativeTimeDirection
    {
        FORWARD, BACKWARD;

        @Override
        public String toString()
        {
            if (this == FORWARD)
                return BVC.forthcomingPeriod();
            else
                return BVC.pastPeriod();
        }
    }

    // value in days
    @NoMetaAccess
    private double relativeTimeLimit = 365 * 100;

    private Date fromDate;
    private Date toDate;

    private boolean isRelative;
    private Integer relativeTime;
    private TimeUtil.TimeUnit relativeTimeUnit;
    private RelativeTimeDirection relativeTimeDirection = RelativeTimeDirection.BACKWARD;

    public Date getFromDate()
    {
        return fromDate;
    }

    public void setFromDate(Date fromDate)
    {
        this.fromDate = fromDate;
    }

    public Date getToDate()
    {
        return toDate;
    }

    public void setToDate(Date toDate)
    {
        this.toDate = toDate;
    }

    public boolean isRelative()
    {
        return isRelative;
    }

    public void setRelative(boolean relative)
    {
        isRelative = relative;
    }

    public Integer getRelativeTime()
    {
        return relativeTime;
    }

    public void setRelativeTime(Integer relativeTime)
    {
        this.relativeTime = relativeTime;
    }

    public TimeUtil.TimeUnit getRelativeTimeUnit()
    {
        return relativeTimeUnit;
    }

    public void setRelativeTimeUnit(TimeUtil.TimeUnit relativeTimeUnit)
    {
        this.relativeTimeUnit = relativeTimeUnit;
    }

    public double getRelativeTimeLimit()
    {
        return relativeTimeLimit;
    }

    public void setRelativeTimeLimit(double relativeTimeLimit)
    {
        this.relativeTimeLimit = relativeTimeLimit;
    }

    public boolean isEmpty()
    {
        if (isRelative)
            return relativeTime == null || relativeTime <= 0 || relativeTimeUnit == null;
        else
            return fromDate == null && toDate == null;
    }

    public RelativeTimeDirection getRelativeTimeDirection()
    {
        return relativeTimeDirection;
    }

    public void setRelativeTimeDirection(RelativeTimeDirection relativeTimeDirection)
    {
        this.relativeTimeDirection = relativeTimeDirection;
    }

    @Override
    public List<Validator> doGetValidators()
    {
        Validator validateRelative = new DependentFieldDelegatingValidator(
                new EqualityValidator(DateTimeRangeInfoMeta.IS_RELATIVE_PROPERTYNAME, Boolean.TRUE, ""),
                new RelativeDateTimeRangeValidator(relativeTimeLimit)
        );

        Validator validateSpecific = new DependentFieldDelegatingValidator(
                new EqualityValidator(DateTimeRangeInfoMeta.IS_RELATIVE_PROPERTYNAME, Boolean.FALSE, ""),
                new DateComparisonValidator(
                    DateTimeRangeInfoMeta.FROM_DATE_PROPERTYNAME,
                    DateTimeRangeInfoMeta.TO_DATE_PROPERTYNAME,
                    DateComparisonValidator.ComparisonOperator.LT)
        );

        return Arrays.asList(validateRelative, validateSpecific);
    }

    @Override
    protected Meta createMetaDelegate()
    {
        return new DateTimeRangeInfoMeta(this);
    }

    public DateTimeRangeInfo createCopy()
    {
        DateTimeRangeInfo copy = new DateTimeRangeInfo();
        copy.merge(this);
        copy.fromDate = fromDate == null ? null : new Date(fromDate.getTime());
        copy.toDate = toDate == null ? null : new Date(toDate.getTime());
        return copy;
    }

    private static final class RelativeDateTimeRangeValidator extends Validator
    {
        private double limit;

        private RelativeDateTimeRangeValidator(double limit)
        {
            super(DateTimeRangeInfoMeta.RELATIVE_TIME_PROPERTYNAME,
                DateTimeRangeInfoMeta.RELATIVE_TIME_UNIT_PROPERTYNAME);
            this.limit = limit;
        }

        @Override
        public List<ValidationResult> validate(Meta instance)
        {
            DateTimeRangeInfo meta = (DateTimeRangeInfo)instance;
            TimeUtil.TimeUnit unit = meta.getRelativeTimeUnit();
            long max;
            if (unit.getCalendarUnit() > TimeUtil.TimeUnit.MONTH.getCalendarUnit())
                max = (long)(limit / (unit.getMilliSeconds() / 1000.0 / 86400.0));
            else
            {
                switch (unit)
                {
                    case MONTH:
                        max = (long)(limit / 30.0);
                        break;
                    case YEAR:
                        max = (long)(limit / 365.0);
                        break;
                    default:
                        throw new AppX("Cannot handle validation of " + unit);
                }
            }

            NumericNumberRange numRangeValidator = new NumericNumberRange(1, max,
                true, DateTimeRangeInfoMeta.RELATIVE_TIME_PROPERTYNAME);

            List<ValidationResult> result = numRangeValidator.validate(meta);
            if (result.size() > 0)
            {
                ValidationResult result0 = result.get(0);
                ValidationResult vr = new ValidationResult(
                    Arrays.asList(DateTimeRangeInfoMeta.RELATIVE_TIME_PROPERTYNAME,
                        DateTimeRangeInfoMeta.RELATIVE_TIME_UNIT_PROPERTYNAME),
                    result0.getReason(), result0.isDisplayInErrorSummary());
                vr.setFieldLabel(result0.getFieldLabel());

                result = Arrays.asList(vr);
            }

            return result;
        }
    }

    @Override
    public String getDefaultFieldLabel(String propertyName)
    {
        if (propertyName.equals(DateTimeRangeInfoMeta.FROM_DATE_PROPERTYNAME))
            return BVC.fromDate();
        else if (propertyName.equals(DateTimeRangeInfoMeta.TO_DATE_PROPERTYNAME))
            return BVC.toDate();
        else
            return super.getDefaultFieldLabel(propertyName);
    }
}
