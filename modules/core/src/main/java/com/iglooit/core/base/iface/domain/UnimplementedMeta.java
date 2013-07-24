package com.iglooit.core.base.iface.domain;

import com.iglooit.commons.iface.domain.meta.Meta;

import java.util.List;
import java.util.Map;

final class UnimplementedMeta implements Meta
{
    private Class metaClass;

    UnimplementedMeta(Class metaClass)
    {
        this.metaClass = metaClass;
    }

    public <X> X get(String propertyName)
    {
        throw new MetaDelegateUnimplementedException(exceptionString());
    }

    public String getPropertyTypeName(String propertyName)
    {
        throw new MetaDelegateUnimplementedException(exceptionString());
    }

    public Map<String, Object> getProperties()
    {
        throw new MetaDelegateUnimplementedException(exceptionString());
    }

    public List<String> getPropertyNames()
    {
        throw new MetaDelegateUnimplementedException(exceptionString());
    }

    public void set(String propertyName, Object value)
    {
        throw new MetaDelegateUnimplementedException(exceptionString());
    }

    private String exceptionString()
    {
        return "Meta delegate not generated, or not registered in factory for: " + metaClass.getName();
    }
}
