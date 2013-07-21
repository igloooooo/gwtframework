package com.iglooit.core.base.client.model;

import com.extjs.gxt.ui.client.data.ModelData;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class GModelData implements ModelData
{
    private HashMap<String, Object> data = new HashMap<String, Object>();

    public HashMap<String, Object> getData()
    {
        return data;
    }

    public void setData(HashMap<String, Object> data)
    {
        this.data = data;
    }

    /**
     * @param setupData - the map values in the format {key, value, key, value, ...}
     */
    public GModelData(Object... setupData)
    {
        //oh, so evil...
        //loads the map based on the order of the values
        boolean isKey = true;
        String key = "";
        for (Object o : setupData)
        {
            if (isKey)
            {
                key = (String)o;
            }
            else
            {
                data.put(key, o);
            }
            isKey = !isKey;
        }
    }

    public <X> X get(String property)
    {
        return (X)data.get(property);
    }

    public Map<String, Object> getProperties()
    {
        return data;
    }

    public Collection<String> getPropertyNames()
    {
        return data.keySet();
    }

    public <X> X remove(String property)
    {
        X tmp = (X)data.get(property);
        data.remove(property);
        return tmp;
    }

    public <X> X set(String property, X value)
    {
        data.put(property, value);
        return value;
    }
}
