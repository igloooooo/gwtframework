package com.iglooit.core.base.client.mvp;

import com.clarity.core.base.client.widget.combobox.ClaritySearchComboBox;
import com.clarity.core.base.iface.domain.ValidatableMeta;

public class ClaritySearchComboBoxBinder extends MutableBinder<String>
{
    public ClaritySearchComboBoxBinder(ClaritySearchComboBox combo,
                                       ValidatableMeta validatableMeta,
                                       String metaFieldName)
    {
        super(combo, validatableMeta, metaFieldName);
    }
}
