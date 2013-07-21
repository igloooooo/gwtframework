package com.iglooit.core.base.client.composition.presenter;

import com.clarity.core.base.client.composition.display.FormWidgetContainer;
import com.clarity.core.base.iface.domain.ValidatableMeta;

public class FormBinderContainer extends BinderContainer
{
    public FormBinderContainer(FormWidgetContainer widgetContainer, ValidatableMeta validatableMeta)
    {
        super(widgetContainer);
        for (String propertyName : widgetContainer.getDeclaredWidgetPropertyNames())
            add(propertyName, validatableMeta);
    }
}
