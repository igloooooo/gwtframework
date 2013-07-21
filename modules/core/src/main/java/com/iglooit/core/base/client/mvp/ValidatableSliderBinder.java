package com.iglooit.core.base.client.mvp;

import com.clarity.core.base.iface.domain.HasValidatingValue;
import com.clarity.core.base.iface.domain.ValidatableMeta;

public class ValidatableSliderBinder extends MutableBinder<Integer>
{
    public ValidatableSliderBinder(HasValidatingValue<Integer> integerHasValidatingValue,
                                   ValidatableMeta validatableMeta,
                                   String metaFieldName)
    {
        super(integerHasValidatingValue, validatableMeta, metaFieldName);
        if (validatableMeta.get(metaFieldName) == null)
            validatableMeta.set(metaFieldName, 0);
    }
}
