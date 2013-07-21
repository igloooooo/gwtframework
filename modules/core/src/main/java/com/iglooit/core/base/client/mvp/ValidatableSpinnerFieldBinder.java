package com.iglooit.core.base.client.mvp;

import com.clarity.core.base.client.widget.form.ValidatableSpinnerField;
import com.clarity.core.base.iface.domain.ValidatableMeta;
import com.clarity.commons.iface.domain.Validator;
import com.clarity.core.base.iface.validation.NumberRange;
import com.clarity.core.base.iface.validation.RequiredField;
import com.clarity.commons.iface.type.Option;

public class ValidatableSpinnerFieldBinder extends MutableBinder<String>
{
    public static final String REQUIRED_LABEL_SEPARATOR = "<span class='required'>*</span>";
    public static final String DEFAULT_LABEL_SEPARATOR = ":";

    public ValidatableSpinnerFieldBinder(ValidatableSpinnerField field,
                                         ValidatableMeta validatableMeta,
                                         String metaFieldName)
    {
        super(field, validatableMeta, metaFieldName);

        for (Validator validator : validatableMeta.getValidators(Option.some(metaFieldName)))
        {
            if (validator instanceof RequiredField)
            {
                field.getField().setAllowBlank(false);
                if (field.getField().getLabelSeparator() == null)
                    field.getField().setLabelSeparator(DEFAULT_LABEL_SEPARATOR);

                String previousLabelSeparator = field.getField().getLabelSeparator();
                String newLabelSeparator;
                if (previousLabelSeparator.contains(REQUIRED_LABEL_SEPARATOR))
                    newLabelSeparator = previousLabelSeparator;
                else
                    newLabelSeparator = REQUIRED_LABEL_SEPARATOR + previousLabelSeparator;

                field.getField().setLabelSeparator(newLabelSeparator);

                // this forces a re-render
                field.setFieldLabel(field.getFieldLabel());
            }

            if (validator instanceof NumberRange)
            {
                field.getField().setMaxValue(((NumberRange)validator).getMaxValue());
                field.getField().setMinValue(((NumberRange)validator).getMinValue());
            }
        }
    }
}
