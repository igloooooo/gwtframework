package com.iglooit.core.base.client.widget.combobox;

import java.util.List;

public interface IComboBox<T>
{
    void updateStore(List<T> list);

    void maskCombo();

    void unmaskCombo();

    List<T> getSelectedItems();
}
