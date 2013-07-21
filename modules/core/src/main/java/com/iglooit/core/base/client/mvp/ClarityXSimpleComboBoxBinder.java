package com.iglooit.core.base.client.mvp;

import com.clarity.core.base.client.widget.combobox.ClarityXSimpleComboBox;
import com.clarity.core.base.iface.domain.ValidatableMeta;

import java.util.List;

public class ClarityXSimpleComboBoxBinder extends MutableBinder<List<String>>
{
    private List<String> storeList;
    private boolean initFinished = false;
    private boolean allSelect = false;

    public ClarityXSimpleComboBoxBinder(ClarityXSimpleComboBox xComboBox,
                                        ValidatableMeta meta,
                                        String attribute)
    {
        super(xComboBox, meta, attribute);
        initFinished = true;
        saveStore();
        saveSelectAllState();
    }

    @Override
    public void saveValue()
    {
        saveStore();
        saveSelectAllState();
        super.saveValue();
    }

    private void saveStore()
    {
        ClarityXSimpleComboBox comboBox = (ClarityXSimpleComboBox)getValidatingValue();
        storeList = comboBox.getStoreList();
    }

    private void saveSelectAllState()
    {
        ClarityXSimpleComboBox comboBox = (ClarityXSimpleComboBox)getValidatingValue();
        allSelect = comboBox.getField().isSelectAll();
    }

    @Override
    public void restoreOriginalValue()
    {
        if (initFinished)
        {
            restoreStore();
            restoreSelectAllState();
            super.restoreOriginalValue();
        }
    }

    private void restoreSelectAllState()
    {
        ClarityXSimpleComboBox comboBox = (ClarityXSimpleComboBox)getValidatingValue();
        comboBox.getField().setSelectAll(allSelect);
    }

    private void restoreStore()
    {
        ClarityXSimpleComboBox comboBox = (ClarityXSimpleComboBox)getValidatingValue();
        comboBox.getField().updateStore(storeList);
    }
}
