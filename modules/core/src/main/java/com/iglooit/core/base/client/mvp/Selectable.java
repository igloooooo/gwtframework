package com.iglooit.core.base.client.mvp;

import com.google.gwt.event.logical.shared.HasSelectionHandlers;

public interface Selectable<T> extends HasSelectionHandlers<T>
{
    void select(T value);
}
