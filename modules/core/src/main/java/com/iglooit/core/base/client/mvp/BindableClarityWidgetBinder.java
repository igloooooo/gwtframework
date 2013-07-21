package com.iglooit.core.base.client.mvp;

import com.clarity.core.base.iface.domain.HasValidatingValue;
import com.clarity.core.base.iface.domain.ValidatableMeta;

public class BindableClarityWidgetBinder extends MutableBinder
{
    public BindableClarityWidgetBinder(HasValidatingValue hasValidatingValue, 
                                       ValidatableMeta validatableMeta,
                                       String metaFieldName)
    {
        super(hasValidatingValue, validatableMeta, metaFieldName);
    }
}
