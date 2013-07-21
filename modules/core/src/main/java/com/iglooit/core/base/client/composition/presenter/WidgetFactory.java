package com.iglooit.core.base.client.composition.presenter;

import com.clarity.core.base.client.composition.display.FormMode;
import com.clarity.core.base.client.mvp.ClarityField;

public interface WidgetFactory
{
    ClarityField getField(String propertyName, FormMode formMode);
}
