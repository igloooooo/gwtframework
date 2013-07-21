package com.iglooit.core.base.client.widget.gridviewcombobox;

import com.clarity.core.base.client.model.SimpleModelValue;
import com.clarity.core.base.client.view.BaseViewConstants;
import com.clarity.core.base.client.view.ClarityStyle;
import com.clarity.core.base.client.widget.combobox.IComboBox;
import com.clarity.core.base.client.widget.grid.ClarityGrid;
import com.clarity.core.base.client.widget.grid.ClaritySimpleGrid;
import com.clarity.commons.iface.domain.I18NFactoryProvider;
import com.clarity.core.lib.iface.TypeConverterTwoWay;

import java.util.ArrayList;
import java.util.List;

public class XSimpleGridViewComboBox extends XGridViewComboBox<SimpleModelValue<String>>
    implements IComboBox<String>
{
    private static final BaseViewConstants BASEVC = I18NFactoryProvider.get(BaseViewConstants.class);
    private ClaritySimpleGrid<String> simpleGrid;

    private TypeConverterTwoWay<String, String> typeConverterTwoWay =
        new TypeConverterTwoWay<String, String>()
        {
            @Override
            public String convertToNew(String s)
            {
                if ("All".equalsIgnoreCase(s))
                    return BASEVC.selectAll();
                return s;
            }

            @Override
            public String convertToOld(String s)
            {
                if (BASEVC.selectAll().equals(s))
                    return "All";
                return s;
            }
        };

    @Override
    public void updateStore(List<String> list)
    {
        List<String> modelList = new ArrayList<String>();
        if (list != null)
            for (String item : list)
            {
                modelList.add(typeConverterTwoWay.convertToNew(item));
            }
        simpleGrid.addSimpleStore(modelList);
        setRawValue("");
    }

    @Override
    public void maskCombo()
    {
        if (isRendered() && !isMasked())
            mask("", ClarityStyle.MASK_FIELD);
    }

    @Override
    public void unmaskCombo()
    {
        if (isRendered() && isMasked())
            unmask();
    }

    @Override
    public List<String> getSelectedItems()
    {
        List<String> selectedModels = simpleGrid.getSelectedValues();
        List<String> selectedStrings = new ArrayList<String>();
        if (selectedModels != null)
        {
            for (String model : selectedModels)
                selectedStrings.add(typeConverterTwoWay.convertToOld(model));
        }
        return selectedStrings;
    }

    @Override
    public ClarityGrid<SimpleModelValue<String>> getGrid()
    {
        simpleGrid = new ClaritySimpleGrid<String>(true, true)
        {
            @Override
            public String getColumnName()
            {
                return "Workgroup";
            }

            @Override
            public String getLabel()
            {
                return null;
            }
        };
        simpleGrid.getContentPanel().setHeight(100);
        simpleGrid.getContentPanel().setHeaderVisible(false);
        return simpleGrid;
    }

}
