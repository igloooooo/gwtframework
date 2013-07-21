package com.iglooit.core.base.client.mvp;

import com.clarity.core.base.client.widget.form.ValidatableCheckBox;
import com.clarity.core.base.iface.domain.ValidatableMeta;

public class ValidatableCheckBoxBinder extends MutableBinder<Boolean>
{
    public ValidatableCheckBoxBinder(ValidatableCheckBox field,
                                     ValidatableMeta validatableMeta,
                                     String metaFieldName)
    {
        super(field, validatableMeta, metaFieldName);
    }


}
