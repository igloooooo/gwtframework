package com.iglooit.core.base.client.widget.grid;

import com.clarity.core.base.client.model.SimpleModelValue;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ClaritySimpleRemovablerGrid<T> extends ClarityRemovableGrid<SimpleModelValue<T>>
{
    public static final int COLUMNWIDTH = 200;

    protected ClaritySimpleRemovablerGrid(boolean checkBox, boolean checkBoxFirstColumn,
                                          boolean showAddRemoveToolbar)
    {
        super(checkBox, checkBoxFirstColumn, showAddRemoveToolbar);
    }

    public abstract String getColumnName();

    @Override
    public SimpleModelValue<T> getEmptyMetaModelData()
    {
        return new SimpleModelValue<T>();
    }

    @Override
    public List<ColumnConfig> getColumnConfig()
    {
        return Arrays.asList(new ColumnConfig(SimpleModelValue.VALUE, getColumnName(), COLUMNWIDTH));
    }

    public List<T> getStoreSimpleList()
    {
        return convertFromModelToSimple(getStoreList());
    }


    public List<T> getSelectedValues()
    {
        return convertFromModelToSimple(getGrid().getSelectionModel().getSelectedItems());
    }

    @Override
    public String getLabel()
    {
        return null;
    }

    public void addSimpleStore(List<T> simpleList)
    {
        updateStore(convertFromSimpleToModel(simpleList));
    }

    private List<T> convertFromModelToSimple(List<SimpleModelValue<T>> modelList)
    {
        List<T> list = new ArrayList<T>();
        for (SimpleModelValue<T> simpleGridValue : modelList)
        {
            list.add(simpleGridValue.getValue());
        }
        return list;
    }

    private List<SimpleModelValue<T>> convertFromSimpleToModel(List<T> simpleList)
    {
        List<SimpleModelValue<T>> list = new ArrayList<SimpleModelValue<T>>();
        for (T simpleValue : simpleList)
        {
            list.add(new SimpleModelValue<T>(simpleValue));
        }
        return list;
    }


    public boolean containValue(T value)
    {
        for (SimpleModelValue<T> item : getStoreList())
        {
            if (item.getValue().equals(value))
                return true;
        }
        return false;
    }


    public void selectValue(T value)
    {
        for (SimpleModelValue<T> item : getStoreList())
        {
            if (item.getValue().equals(value))
                getGrid().getSelectionModel().select(item, true);
        }
    }
}
