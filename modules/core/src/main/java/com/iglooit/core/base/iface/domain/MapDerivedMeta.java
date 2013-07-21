package com.iglooit.core.base.iface.domain;

import com.clarity.commons.iface.annotation.NoMetaAccess;
import com.clarity.commons.iface.domain.meta.Meta;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@NoMetaAccess
public class MapDerivedMeta implements Meta, Serializable, IsSerializable
{
    private Map<String, Object> values;
    private Map<String, String> classNames;

    public MapDerivedMeta()
    {
        values = new HashMap<String, Object>();
        classNames = new HashMap<String, String>();
    }

    public MapDerivedMeta(Map<String, Object> values,
                          Map<String, String> classNames)
    {
        this.values = values;
        this.classNames = classNames;
    }

    protected Map<String, Object> getValues()
    {
        return values;
    }

    protected void setValues(Map<String, Object> values)
    {
        this.values = values;
    }

    protected Map<String, String> getClassNames()
    {
        return classNames;
    }

    protected void setClassNames(Map<String, String> classNames)
    {
        this.classNames = classNames;
    }

    public <X> X get(String propertyName)
    {
        return (X)values.get(propertyName);
    }

    public String getPropertyTypeName(String propertyName)
    {
        return classNames.get(propertyName);
    }

    public Map<String, Object> getProperties()
    {
        return new HashMap<String, Object>(values);
    }

    public List<String> getPropertyNames()
    {
        return new ArrayList<String>(values.keySet());
    }

    public void set(String propertyName, Object value)
    {
//        if (!values.containsKey(propertyName))
//            throw new AppX("Propertyname not found: " + propertyName);
        values.put(propertyName, value);
    }
}
