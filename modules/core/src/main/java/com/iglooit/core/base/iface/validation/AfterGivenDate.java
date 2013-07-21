package com.iglooit.core.base.iface.validation;

import com.clarity.commons.iface.domain.meta.Meta;
import com.clarity.core.lib.iface.BssTimeUtil;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.domain.Validator;
import com.clarity.commons.iface.domain.I18NFactoryProvider;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class AfterGivenDate extends Validator
{
    private Date earliestValidDate;
    private String propertyName;
    private static final ValidationConstants VC = I18NFactoryProvider.get(ValidationConstants.class);

    public AfterGivenDate(String propertyName, Date earliestValidDate)
    {
        super(propertyName);
        this.propertyName = propertyName;
        this.earliestValidDate = earliestValidDate;
    }

    public List<ValidationResult> validate(Meta instance)
    {
        Date propertyValue = (Date)instance.get(propertyName);
        if (propertyValue == null)
            return Collections.emptyList();

        // use only date portion to compare
        Date today = this.earliestValidDate;
        Date todayDate = BssTimeUtil.getBeginningOfDay(today);
        Date inputDate = BssTimeUtil.getBeginningOfDay(propertyValue);
        if (inputDate.before(todayDate))
            return Arrays.asList(new ValidationResult(getTags(), VC.dateIsBeforeToday()));
        return Collections.emptyList();
    }
}
