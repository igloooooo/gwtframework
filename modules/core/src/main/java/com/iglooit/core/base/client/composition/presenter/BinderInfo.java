package com.iglooit.core.base.client.composition.presenter;

import com.clarity.core.base.iface.domain.ValidatableMeta;
import com.clarity.commons.iface.type.NonSerOpt;

class BinderInfo
{
    private final String propertyName;
    private final ValidatableMeta validatableMeta;
    private final NonSerOpt<BinderFactory> binderFactory;

    protected BinderInfo(String propertyName,
                         ValidatableMeta validatableMeta,
                         BinderFactory binderFactory)
    {
        this.propertyName = propertyName;
        this.validatableMeta = validatableMeta;
        this.binderFactory = NonSerOpt.option(binderFactory);
    }

    public BinderInfo(String propertyName, ValidatableMeta validatableMeta)
    {
        this(propertyName, validatableMeta, null);
    }

    public String getPropertyName()
    {
        return propertyName;
    }

    public ValidatableMeta getValidatableMeta()
    {
        return validatableMeta;
    }

    public NonSerOpt<BinderFactory> getBinderFactory()
    {
        return binderFactory;
    }
}
