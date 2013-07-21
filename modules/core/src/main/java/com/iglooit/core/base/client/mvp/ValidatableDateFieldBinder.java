package com.iglooit.core.base.client.mvp;

import com.clarity.core.base.client.widget.form.ValidatableDateField;
import com.clarity.core.base.iface.domain.ValidatableMeta;

import java.util.Date;

public class ValidatableDateFieldBinder extends ClarityFieldBinder<Date>
{
    public ValidatableDateFieldBinder(ValidatableDateField field,
                                      ValidatableMeta validatableMeta,
                                      String metaFieldName)
    {
        super(field, validatableMeta, metaFieldName);
    }
}
