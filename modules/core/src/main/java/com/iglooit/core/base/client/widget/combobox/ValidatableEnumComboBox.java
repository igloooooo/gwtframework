package com.iglooit.core.base.client.widget.combobox;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class ValidatableEnumComboBox<T extends Enum<T>> extends ValidatableComboBox<T>
{

    private List<T> fullDataSet;
    private List<T> restrictedDataSet;

    public ValidatableEnumComboBox(Class<T> enumClass)
    {
        this(EnumSet.allOf(enumClass));
        fullDataSet = new ArrayList<T>(EnumSet.allOf(enumClass));
    }

    public ValidatableEnumComboBox(EnumSet<T> allowedValues)
    {
        super(new ArrayList<T>(allowedValues));
    }

    public List<T> getFullDataSet()
    {
        return fullDataSet;
    }

    public void setFullDataSet(List<T> fullDataSet)
    {
        this.fullDataSet = fullDataSet;
    }

    public List<T> getRestrictedDataSet()
    {
        return restrictedDataSet;
    }

    public void setRestrictedDataSet(List<T> restrictedDataSet)
    {
        this.restrictedDataSet = restrictedDataSet;
        updateStore(new ArrayList<T>(restrictedDataSet));
    }

    public void restoreDataSet()
    {
        setRestrictedDataSet(fullDataSet);
    }
}
