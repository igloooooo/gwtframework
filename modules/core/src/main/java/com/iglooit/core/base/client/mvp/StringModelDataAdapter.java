package com.iglooit.core.base.client.mvp;

import com.extjs.gxt.ui.client.data.ModelData;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class StringModelDataAdapter implements ModelData
{
    private String value;

    @Override
    public <X> X get(String s)
    {
        return (X)value;
    }

    @Override
    public Map<String, Object> getProperties()
    {
        return new HashMap<String, Object>();
    }

    @Override
    public Collection<String> getPropertyNames()
    {
        return Collections.emptyList();
    }

    @Override
    public <X> X remove(String s)
    {
        return null;
    }

    @Override
    public <X> X set(String s, X x)
    {
        value = (String)x;
        return x;
    }
}
