package com.iglooit.core.base.client.mvp;

import com.clarity.core.base.client.widget.form.ValidatableTextFieldBase;
import com.clarity.core.base.iface.domain.ValidatableMeta;
import com.clarity.commons.iface.domain.Validator;
import com.clarity.commons.iface.type.Option;

public class ValidatableTextFieldBaseBinder<T> extends ClarityFieldBinder<T>
{
    public ValidatableTextFieldBaseBinder(ValidatableTextFieldBase<T> field,
                                          ValidatableMeta validatableMeta,
                                          String metaFieldName)
    {
        super(field, validatableMeta, metaFieldName);
        for (Validator validator : validatableMeta.getValidators(Option.some(metaFieldName)))
        {
//            if (validator instanceof MaxStringLength)
//                field.setMaxLength(((MaxStringLength)validator).getMaxLength());
//            if (validator instanceof MinStringLength)
//                field.setMinLength(((MinStringLength)validator).getMinLength());
        }
    }
}
