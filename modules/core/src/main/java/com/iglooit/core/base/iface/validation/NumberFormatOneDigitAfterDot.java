package com.iglooit.core.base.iface.validation;

import com.iglooit.commons.iface.domain.I18NFactoryProvider;
import com.iglooit.commons.iface.domain.ValidationResult;
import com.iglooit.commons.iface.domain.Validator;
import com.iglooit.commons.iface.domain.meta.Meta;
import com.google.gwt.i18n.client.NumberFormat;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NumberFormatOneDigitAfterDot extends Validator
{
    private final String propertyName;
    private static final String NUMBER_FORMAT = "##0.0";

    private static final ValidationConstants VC = I18NFactoryProvider.get(ValidationConstants.class);

    public NumberFormatOneDigitAfterDot(String propertyName)
    {
        super(propertyName);
        this.propertyName = propertyName;
    }

    public List<ValidationResult> validate(Meta instance)
    {
        Double number = null;
        if (instance.get(propertyName) instanceof Double)
        {
            number = instance.get(propertyName);
        }
        else
        {
            String propertyValue = instance.get(propertyName);
            if (propertyValue == null || propertyValue.equals(""))
                return Collections.emptyList();

            number = Double.parseDouble(propertyValue);
        }

        String numberStr = NumberFormat.getFormat(NUMBER_FORMAT).format(number);
        Double numberValidate = NumberFormat.getFormat(NUMBER_FORMAT).parse(numberStr);
        if (!number.equals(numberValidate))
            return Arrays.asList(
                    new ValidationResult(getTags(), VC.invalidNumberFormatOneDigitAfterDot()));
        return Collections.emptyList();
    }
}
