package com.iglooit.core.base.client.widget.grid;

import com.clarity.core.base.client.model.SimpleModelValue;
import com.extjs.gxt.ui.client.widget.grid.ColumnConfig;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class ClaritySimpleGrid<T> extends ClarityBasicGrid<SimpleModelValue<T>>
{
    public static final int COLUMNWIDTH = 200;

    protected ClaritySimpleGrid(boolean checkBox, boolean checkBoxFirstColumn)
    {
        super(checkBox, checkBoxFirstColumn);
    }

    public abstract String getColumnName();

    @Override
    public List<ColumnConfig> getColumnConfig()
    {
        return Arrays.asList(new ColumnConfig(SimpleModelValue.VALUE, getColumnName(), COLUMNWIDTH));
    }

    public List<T> getSimpleStoreList()
    {
        return convertFromModelToSimple(getStoreList());
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

    public void addSimpleStore(List<T> simpleList)
    {
        updateStore(convertFromSimpleToModel(simpleList));
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

    public List<T> getSelectedValues()
    {
        return convertFromModelToSimple(getGrid().getSelectionModel().getSelectedItems());
    }

    /**
     * @return Returns the currently selected value; null if nothing is selected
     */
    public T getSelectedValue()
    {
        final SimpleModelValue<T> selected = getGrid().getSelectionModel().getSelectedItem();
        return selected == null ? null : selected.getValue();
    }
}
