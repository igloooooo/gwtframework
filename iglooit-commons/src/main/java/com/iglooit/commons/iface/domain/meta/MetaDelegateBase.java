package com.iglooit.commons.iface.domain.meta;

import com.clarity.commons.iface.annotation.NoMetaAccess;
import com.clarity.commons.iface.type.NoMetaPropertyNameX;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// supress metas of meta delegates
@NoMetaAccess
public abstract class MetaDelegateBase implements Meta
{
    private final DelegatingMetaEntity instance;

    public MetaDelegateBase(DelegatingMetaEntity instance)
    {
        this.instance = instance;
    }

    public DelegatingMetaEntity getInstance()
    {
        return instance;
    }

    public <X> X get(String propertyName)
    {
        if (propertyName.contains(FIELD_META_SPLIT))
        {
            String myField = propertyName.split(FIELD_META_SPLIT_REGEX)[0];
            String others = propertyName.substring(myField.length() + 1);
            return (X)(((Meta)get(myField)).get(others));
        }
        else
            throw new NoMetaPropertyNameX("get", propertyName);
    }

    public Map<String, Object> getProperties()
    {
        Map<String, Object> props = new HashMap<String, Object>();
        for (String propertyName : getPropertyNames())
            props.put(propertyName, get(propertyName));
        return props;
    }

    public List<String> getPropertyNames()
    {
        return Collections.emptyList();
    }

    public void set(String propertyName, Object value)
    {
        if (propertyName.contains(FIELD_META_SPLIT))
        {
            String myField = propertyName.split(FIELD_META_SPLIT_REGEX)[0];
            String others = propertyName.substring(myField.length() + 1);
            ((Meta)get(myField)).set(others, value);
        }
        else
            throw new NoMetaPropertyNameX("set", propertyName);
    }

    public String getPropertyTypeName(String propertyName)
    {
        if (propertyName.contains(FIELD_META_SPLIT))
        {
            String myField = propertyName.split(FIELD_META_SPLIT_REGEX)[0];
            String others = propertyName.substring(myField.length() + 1);
            return ((Meta)get(myField)).getPropertyTypeName(others);
        }
        else
            throw new NoMetaPropertyNameX("getPropertyTypeName", propertyName);
    }
}
