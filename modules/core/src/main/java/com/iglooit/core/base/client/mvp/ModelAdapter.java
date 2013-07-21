package com.iglooit.core.base.client.mvp;

import com.clarity.core.base.iface.domain.JpaDomainEntity;
import com.extjs.gxt.ui.client.data.ModelData;

import java.util.Collection;
import java.util.Map;

public class ModelAdapter<T extends JpaDomainEntity> implements ModelData
{
    private T domainEntity;


    public ModelAdapter(T domainEntity)
    {
        this.domainEntity = domainEntity;
    }

    @Override
    public <X> X get(String s)
    {
        return (X)domainEntity.get(s);
    }

    @Override
    public Map<String, Object> getProperties()
    {
        return domainEntity.getProperties();
    }

    @Override
    public Collection<String> getPropertyNames()
    {
        return domainEntity.getPropertyNames();
    }

    public <X> X get(String property, X valueWhenNull)
    {
        X result = (X)domainEntity.get(property);
        return result != null ? result : valueWhenNull;
    }

    @Override
    public <X> X remove(String s)
    {
        //does nothing
        return null;
    }

    @Override
    public <X> X set(String propertyName, X value)
    {
        domainEntity.set(propertyName, value);
        return value;
    }

    public T getDomainEntity()
    {
        return domainEntity;
    }
}
