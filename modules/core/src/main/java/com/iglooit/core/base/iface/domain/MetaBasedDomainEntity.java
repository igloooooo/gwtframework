package com.iglooit.core.base.iface.domain;

import com.iglooit.commons.iface.annotation.NoMetaAccess;
import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.AppX;

import java.util.HashMap;
import java.util.Map;

@NoMetaAccess
public abstract class MetaBasedDomainEntity extends DomainEntity

{
    // this is a serializable delegate, unlike the regular DomainEntity transient delegate
    private Meta nonTransientDelegate;

    protected MetaBasedDomainEntity()
    {
    }

    protected MetaBasedDomainEntity(Meta nonTransientDelegate)
    {
        this.nonTransientDelegate = nonTransientDelegate;
    }

    public Meta getMetaDelegate()
    {
        if (nonTransientDelegate == null)
            setMetaDelegate(createMetaDelegate());
        return nonTransientDelegate;
    }

    @Override
    protected Meta createMetaDelegate()
    {
        return new MapDerivedMeta();
    }


    @Override
    public void setMetaDelegate(Meta metaDelegate)
    {
        nonTransientDelegate = metaDelegate;
        super.setMetaDelegate(metaDelegate);
    }

    public Meta getNonTransientDelegate()
    {
        return nonTransientDelegate;
    }

    public void setNonTransientDelegate(Meta nonTransientDelegate)
    {
        this.nonTransientDelegate = nonTransientDelegate;
    }

    // clones only the meta attributes
    public <T extends MetaBasedDomainEntity> void copyMeta(T source)
    {
        if (!source.getClass().equals(getClass()))
            throw new AppX("Cannot copy meta of different classes");

        Meta meta = getMetaDelegate();
        if (meta instanceof MapDerivedMeta)
        {
            MapDerivedMeta mapMeta = (MapDerivedMeta)meta;
            mapMeta.setValues(new HashMap<String, Object>(source.getProperties()));
        }
        else
        {
            // slow by slow if it's not a map backed meta
            for (Map.Entry<String, Object> prop : getProperties().entrySet())
            {
                set(prop.getKey(), prop.getValue());
            }
        }
    }
}
