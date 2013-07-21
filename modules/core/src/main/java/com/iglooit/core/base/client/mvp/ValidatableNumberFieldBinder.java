package com.iglooit.core.base.client.mvp;

import com.clarity.core.base.client.widget.form.ValidatableNumberField;
import com.clarity.core.base.iface.domain.ValidatableMeta;

public class ValidatableNumberFieldBinder extends ClarityFieldBinder<String>
{
    public ValidatableNumberFieldBinder(ValidatableNumberField field,
                                        ValidatableMeta validatableMeta,
                                        String metaFieldName)
    {
        super(field, validatableMeta, metaFieldName);
    }
}
