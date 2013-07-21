package com.iglooit.core.base.iface.validation;

import com.clarity.core.lib.iface.BssTimeUtil;
import com.clarity.core.base.client.widget.form.ClarityTimeField;
import com.clarity.core.base.iface.domain.DomainEntity;
import com.clarity.commons.iface.domain.meta.Meta;
import com.clarity.commons.iface.domain.ValidationResult;
import com.clarity.commons.iface.domain.Validator;
import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.commons.iface.type.Option;
import com.clarity.commons.iface.util.StringUtil;
import com.google.gwt.i18n.client.DateTimeFormat;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class FirstDateTimeBeforeSecondDateTime extends Validator
{
    private String firstDateName;
    private String firstTimeName;
    private String secondDateName;
    private String secondTimeName;
    private static final ValidationConstants VC = I18NFactoryProvider.get(ValidationConstants.class);

    public FirstDateTimeBeforeSecondDateTime(String firstDateName,
                                             String firstTimeName,
                                             String secondDateName,
                                             String secondTimeName)
    {
        super(firstDateName,
            firstTimeName,
            secondDateName,
            secondTimeName);
        this.firstDateName = firstDateName;
        this.firstTimeName = firstTimeName;
        this.secondDateName = secondDateName;
        this.secondTimeName = secondTimeName;
    }

    public List<ValidationResult> validate(Meta instance)
    {
        if (instance.<String>get(firstDateName) == null
            || StringUtil.isEmpty(instance.<String>get(firstTimeName))
            || instance.<String>get(secondDateName) == null
            || StringUtil.isEmpty(instance.<String>get(secondTimeName)))
            return Collections.emptyList();
        Date firstDate = BssTimeUtil.appendDateAndTime(instance.<Date>get(firstDateName),
            DateTimeFormat.getFormat(ClarityTimeField.DISPLAY_TIME_FORMAT).parse(
                instance.<String>get(firstTimeName)));
        Date secondDate = BssTimeUtil.appendDateAndTime(instance.<Date>get(secondDateName),
            DateTimeFormat.getFormat(ClarityTimeField.DISPLAY_TIME_FORMAT).parse(
                instance.<String>get(secondTimeName)));

        if (firstDate.after(secondDate))
        {
            notifyHandlers(instance, VC.invalidDateCompareMsg());
            return Arrays.asList(new ValidationResult(getTags(), VC.invalidDateCompareMsg()));
        }
        else
            notifyHandlers(instance, "");
        return Collections.emptyList();
    }

    private void notifyHandlers(Meta instance, String message)
    {
        if (StringUtil.isEmpty(message))
        {
            ((DomainEntity)instance).notifyValidationHandlers(Collections.<ValidationResult>emptyList(),
                Option.some(firstDateName));
            ((DomainEntity)instance).notifyValidationHandlers(Collections.<ValidationResult>emptyList(),
                Option.some(firstTimeName));
            ((DomainEntity)instance).notifyValidationHandlers(Collections.<ValidationResult>emptyList(),
                Option.some(secondDateName));
            ((DomainEntity)instance).notifyValidationHandlers(Collections.<ValidationResult>emptyList(),
                Option.some(secondTimeName));
        }
        else
        {
            ValidationResult vrFirst = new ValidationResult(getTags(), message);
            ((DomainEntity)instance).notifyValidationHandlers(Arrays.asList(vrFirst),
                Option.some(firstDateName));
            ((DomainEntity)instance).notifyValidationHandlers(Arrays.asList(vrFirst),
                Option.some(firstTimeName));
            ((DomainEntity)instance).notifyValidationHandlers(Arrays.asList(vrFirst),
                Option.some(secondDateName));
            ((DomainEntity)instance).notifyValidationHandlers(Arrays.asList(vrFirst),
                Option.some(secondTimeName));
        }
    }

}
