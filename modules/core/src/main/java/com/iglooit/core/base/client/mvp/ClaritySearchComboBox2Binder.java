package com.iglooit.core.base.client.mvp;

import com.clarity.core.base.client.widget.combobox.ClaritySearchComboBox2;
import com.clarity.core.base.iface.domain.ValidatableMeta;

public class ClaritySearchComboBox2Binder extends MutableBinder<String>
{
    public ClaritySearchComboBox2Binder(ClaritySearchComboBox2 combo,
                                       ValidatableMeta validatableMeta,
                                       String metaFieldName)
    {
        super(combo, validatableMeta, metaFieldName);
    }
}