package com.iglooit.commons.iface.domain.meta;

import com.iglooit.commons.iface.annotation.BeanLibIgnore;
import com.iglooit.commons.iface.annotation.NoMetaAccess;
import com.iglooit.commons.iface.type.AppX;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class DelegatingMetaEntity implements Serializable, Meta
{
    @NoMetaAccess
    private transient Meta metaDelegate;

    protected abstract Meta createMetaDelegate();

    @BeanLibIgnore
    public synchronized Meta getMetaDelegate()
    {
        if (metaDelegate == null)
        {
            Meta delegate = createMetaDelegate();
            if (delegate == null)
                throw new AppX("Null meta delegate returned for class: " + getClass().getName());
            setMetaDelegate(delegate);
        }
        return metaDelegate;
    }

    @BeanLibIgnore
    public synchronized void setMetaDelegate(Meta metaDelegate)
    {
        this.metaDelegate = metaDelegate;
    }

    public <X> X get(String propertyName)
    {
        return (X)getMetaDelegate().get(propertyName);
    }

    public final Map<String, Object> getProperties()
    {
        return getMetaDelegate().getProperties();
    }

    public List<String> getPropertyNames()
    {
        return getMetaDelegate().getPropertyNames();
    }

    public void set(String propertyName, Object value)
    {
        getMetaDelegate().set(propertyName, value);
    }

    public final String getPropertyTypeName(String propertyName)
    {
        return getMetaDelegate().getPropertyTypeName(propertyName);
    }

    // copy another DE's contents into this. used when the DE reference can't change,
    // e.g. when a binder is associated with a DE.
    public <DE extends DelegatingMetaEntity> void merge(DE other)
    {
        merge(other, getPropertyNames());
    }

    public <DE extends DelegatingMetaEntity> void merge(DE other, Collection<String> propertyNames)
    {
        if (!other.getClass().equals(this.getClass()))
            throw new AppX("Trying to copy different DEs through each other");
        for (String propertyName : propertyNames)
            set(propertyName, other.get(propertyName));
    }
}
