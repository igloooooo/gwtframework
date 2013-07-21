package com.iglooit.core.base.client.mvp;

import com.clarity.core.base.client.widget.form.ValidatableTextArea;
import com.clarity.core.base.iface.domain.ValidatableMeta;

public class ValidatableTextAreaBinder extends ClarityFieldBinder<String>
{
    public ValidatableTextAreaBinder(ValidatableTextArea field,
                                     ValidatableMeta validatableMeta,
                                     String metaFieldName)
    {
        super(field, validatableMeta, metaFieldName);
    }
}
