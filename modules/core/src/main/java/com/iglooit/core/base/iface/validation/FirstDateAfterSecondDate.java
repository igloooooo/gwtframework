package com.iglooit.core.base.iface.validation;

import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.domain.Validator;
import com.clarity.commons.iface.domain.meta.Meta;
import com.clarity.commons.iface.type.Option;
import com.clarity.core.base.client.widget.form.ClarityTimeField;
import com.clarity.core.base.iface.domain.DomainEntity;
import com.clarity.core.lib.iface.BssTimeUtil;
import com.google.gwt.i18n.client.DateTimeFormat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class FirstDateAfterSecondDate extends Validator
{
    private String propertyNameDateFirst;
    private String propertyNameTimeFirst;
    private String propertyNameDateSecond;
    private String propertyNameTimeSecond;
    private static final ValidationConstants VC = I18NFactoryProvider.get(ValidationConstants.class);
    private String timeFormat = ClarityTimeField.DISPLAY_TIME_FORMAT;

    public FirstDateAfterSecondDate(String propertyNameDateFirst, String propertyNameDateSecond)
    {
        super(propertyNameDateFirst, propertyNameDateSecond);
        this.propertyNameDateFirst = propertyNameDateFirst;
        this.propertyNameDateSecond = propertyNameDateSecond;
    }

    public FirstDateAfterSecondDate(String propertyNameDateFirst, String propertyNameTimeFirst,
                                    String propertyNameDateSecond, String propertyNameTimeSecond)
    {
        super(propertyNameDateFirst, propertyNameDateSecond);
        this.propertyNameDateFirst = propertyNameDateFirst;
        this.propertyNameTimeFirst = propertyNameTimeFirst;
        this.propertyNameDateSecond = propertyNameDateSecond;
        this.propertyNameTimeSecond = propertyNameTimeSecond;
    }

    public List<ValidationResult> validate(Meta instance)
    {
        Date firstDate = (Date)instance.get(propertyNameDateFirst);
        String firstTime = (String)instance.get(propertyNameTimeFirst);
        if (firstDate == null)
            return Collections.emptyList();
        Date secondDate = (Date)instance.get(propertyNameDateSecond);
        String secondTime = (String)instance.get(propertyNameTimeSecond);
        if (secondDate == null)
            return Collections.emptyList();


        ValidationResult vrFirst = new ValidationResult(Arrays.asList(propertyNameDateFirst),
            VC.invalidDateCompareMsg());
        ValidationResult vrSecond = new ValidationResult(Arrays.asList(propertyNameDateSecond),
            VC.invalidDateCompareMsg());
        vrFirst.setFieldLabel(propertyNameDateFirst);
        vrSecond.setFieldLabel(propertyNameDateSecond);

        if (firstTime != null)
        {
            Date firstTimeDate = DateTimeFormat.getFormat(getTimeFormat()).
                    parse(firstTime);
            firstDate = BssTimeUtil.appendDateAndTime(firstDate, firstTimeDate);
        }

        if (secondTime != null)
        {
            Date secondTimeDate = DateTimeFormat.getFormat(getTimeFormat()).
                    parse(firstTime);
            secondDate = BssTimeUtil.appendDateAndTime(secondDate, secondTimeDate);
        }

        if (firstDate.before(secondDate))
        {
            ((DomainEntity)instance).notifyValidationHandlers(Arrays.asList(vrFirst),
                Option.some(propertyNameDateFirst));
            ((DomainEntity)instance).notifyValidationHandlers(Arrays.asList(vrSecond),
                Option.some(propertyNameDateSecond));
            return Arrays.asList(new ValidationResult(getTags(), VC.invalidDateCompareMsg()));
        }
        else
        {
            ((DomainEntity)instance).notifyValidationHandlers(new ArrayList<ValidationResult>(),
                Option.some(propertyNameDateFirst));
            ((DomainEntity)instance).notifyValidationHandlers(new ArrayList<ValidationResult>(),
                Option.some(propertyNameDateSecond));
        }
//            return Arrays.asList(new ValidationResult(getTags(), VC.invalidDateCompareMsg()));
        return Collections.emptyList();
    }

    public String getTimeFormat()
    {
        return timeFormat;
    }

    public void setTimeFormat(String timeFormat)
    {
        this.timeFormat = timeFormat;
    }
}
