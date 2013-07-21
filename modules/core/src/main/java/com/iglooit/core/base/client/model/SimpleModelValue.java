package com.iglooit.core.base.client.model;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class SimpleModelValue<T> extends BaseModelData
{
    public static final String VALUE = "value";
    public static final String DISPLAY_VALUE = "displayValue";

    public SimpleModelValue()
    {
        setValue(null);
    }

    public SimpleModelValue(T value)
    {
        setValue(value);
    }

    @SuppressWarnings("unchecked")
    public T getValue()
    {
        return (T)get(VALUE);
    }

    public void setValue(T value)
    {
        set(VALUE, value);
    }

    public String getDisplayValue()
    {
        return get(DISPLAY_VALUE);
    }

    public void setDisplayValue(String value)
    {
        set(DISPLAY_VALUE, value);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleModelValue<T> that = (SimpleModelValue<T>)o;

        if (getValue() == null && that.getValue() == null) return true;
        if (getValue() == null || that.getValue() == null) return false;

        return getValue().equals(that.getValue());
    }

    @Override
    public int hashCode()
    {
        return getValue() != null ? getValue().hashCode() : 0;
    }

}
