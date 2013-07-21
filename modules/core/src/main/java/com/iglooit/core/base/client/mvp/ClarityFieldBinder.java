package com.iglooit.core.base.client.mvp;

import com.clarity.core.base.iface.domain.ValidatableMeta;
import com.clarity.commons.iface.domain.Validator;
import com.clarity.core.base.iface.validation.RequiredField;
import com.clarity.commons.iface.type.Option;
import com.extjs.gxt.ui.client.widget.form.Field;

public class ClarityFieldBinder<T> extends MutableBinder<T>
{
    public static final String REQUIRED_LABEL_SEPARATOR = "<span class='required'>*</span>";
    public static final String DEFAULT_LABEL_SEPARATOR = ":";

    // notice no generics on the TextField parameter; it must be compatible with ValidatableTextFields,
    // which extend TextField<String>, and ComboBoxes which extend TextField<SimpleComboValue<T>>
    public ClarityFieldBinder(ClarityField<T, ?> clarityField,
                              ValidatableMeta validatableMeta,
                              String metaFieldName)
    {
        super(clarityField, validatableMeta, metaFieldName);
        if (clarityField.getField() != null && clarityField.getField() instanceof Field)
        {
            Field field = (Field)clarityField.getField();
            for (Validator validator : validatableMeta.getValidators(Option.some(metaFieldName)))
            {
                if (validator instanceof RequiredField)
                {
//                field.setAllowBlank(false);
                    if (field.getLabelSeparator() == null)
                        field.setLabelSeparator(DEFAULT_LABEL_SEPARATOR);

                    String previousLabelSeparator = field.getLabelSeparator();
                    String newLabelSeparator;
                    if (previousLabelSeparator.contains(REQUIRED_LABEL_SEPARATOR))
                        newLabelSeparator = previousLabelSeparator;
                    else
                        newLabelSeparator = REQUIRED_LABEL_SEPARATOR + previousLabelSeparator;

                    field.setLabelSeparator(newLabelSeparator);

                    // this forces a re-render
                    field.setFieldLabel(field.getFieldLabel());
                }
            }
        }
    }
}
