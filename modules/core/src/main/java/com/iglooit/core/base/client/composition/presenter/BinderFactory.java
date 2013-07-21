package com.iglooit.core.base.client.composition.presenter;

import com.clarity.core.base.client.mvp.Binder;
import com.clarity.core.base.client.mvp.ClarityField;
import com.clarity.core.base.iface.domain.ValidatableMeta;

public interface BinderFactory
{
    Binder createBinder(String propertyName,
                        ValidatableMeta validatableMeta,
                        ClarityField clarityField);
}
