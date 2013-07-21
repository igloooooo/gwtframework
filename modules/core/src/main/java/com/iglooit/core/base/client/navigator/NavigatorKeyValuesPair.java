package com.iglooit.core.base.client.navigator;

import java.util.ArrayList;
import java.util.List;

public class NavigatorKeyValuesPair
{
    private String key;
    private List<String> values = new ArrayList<String>();

    public NavigatorKeyValuesPair(String key, String value)
    {
        this.key = key;
        values.add(value);
    }

    public NavigatorKeyValuesPair(String key, List<String> values)
    {
        this.key = key;
        this.values.addAll(values);
    }

    public String getKey()
    {
        return key;
    }

    public List<String> getValues()
    {
        return values;
    }
}
