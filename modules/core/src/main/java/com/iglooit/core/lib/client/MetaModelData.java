package com.iglooit.core.lib.client;

import com.clarity.commons.iface.domain.meta.Meta;
import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.type.NoMetaPropertyNameX;
import com.clarity.commons.iface.util.StringUtil;
import com.clarity.core.lib.iface.TypeConverter;
import com.extjs.gxt.ui.client.data.ModelComparer;
import com.extjs.gxt.ui.client.data.ModelData;

import java.util.*;

public class MetaModelData<M extends Meta> implements ModelData
{
    // todo ms: check that the semantic behavior of this is appropriate
    // private final M meta;               /* Comment this in order to set meta at runtime */
    private M meta;
    private Set<String> localPropertyNames;
    private Map<String, Object> localProperties = new HashMap<String, Object>();

    private ModelComparer<MetaModelData<M>> comparer;

    public MetaModelData(M meta)
    {
        this.meta = meta;
        this.localPropertyNames = new TreeSet<String>(meta.getPropertyNames());
        getProperties();
    }

    public MetaModelData(M meta, String... propertyNames)
    {
        this.meta = meta;
        this.localPropertyNames = new TreeSet<String>(Arrays.asList(propertyNames));
    }

    public MetaModelData(M meta, Collection<String> propertyNames)
    {
        this.meta = meta;
        this.localPropertyNames = new TreeSet<String>(propertyNames);
        getProperties();
    }

    public M getMeta()
    {
        return meta;
    }

    public static <T extends Meta> List<MetaModelData<T>> wrapInMetaModelData(Collection<T> dfdList)
    {
        List<MetaModelData<T>> metaDfdList = new ArrayList<MetaModelData<T>>();
        for (T dfd : dfdList)
        {
            metaDfdList.add(new MetaModelData<T>(dfd));
        }
        return metaDfdList;
    }

    public static <T extends Meta> List<T> unwrapMetaModelData(Collection<? extends MetaModelData<T>>
                                                                   metaModelDataCollection)
    {
        List<T> entityList = new ArrayList<T>();
        for (MetaModelData<T> tMetaModelData : metaModelDataCollection)
        {
            entityList.add(tMetaModelData.getMeta());
        }
        return entityList;
    }

    public <X> X get(String s)
    {
        try
        {
            return (X)meta.get(s);
        }
        catch (NoMetaPropertyNameX e)
        {
            return (X)localProperties.get(StringUtil.emptyStringIfNull(s));
        }
    }

    public Map<String, Object> getProperties()
    {
        Map<String, Object> ret = new HashMap<String, Object>();
        for (String localPropertyName : localPropertyNames)
            ret.put(localPropertyName, get(localPropertyName));
        return ret;
    }

    public Collection<String> getPropertyNames()
    {
        return localPropertyNames;
    }

    public <X> X remove(String s)
    {
        throw new AppX("don't call remove");
    }

    public <X> X set(String s, X x)
    {
        X oldValue = (X)get(s);
        try
        {
            meta.set(s, x);
        }
        catch (NoMetaPropertyNameX e)
        {
            localPropertyNames.add(StringUtil.emptyStringIfNull(s));
            localProperties.put(StringUtil.emptyStringIfNull(s), x);
        }
        return oldValue;
    }

    public void setMeta(M meta)
    {
        this.meta = meta;
    }

    public static <M extends Meta, MD extends MetaModelData<M>>
    List<MD> getModels(List<M> metas, TypeConverter<M, MD> converter)
    {
        List<MD> mds = new ArrayList<MD>();
        for (M meta : metas)
        {
            MD md = converter.convert(meta);
            if (md != null)
                mds.add(md);
        }
        return mds;
    }

    public void setComparer(ModelComparer<MetaModelData<M>> comparer)
    {
        this.comparer = comparer;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof MetaModelData)
        {
            if (comparer != null)
                return comparer.equals(this, (MetaModelData)obj);
            else
                return ((MetaModelData)obj).getMeta().equals(getMeta());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode()
    {
        return meta.hashCode();
    }

    public String getDuplicateAttribute()
    {
        return "";
    }
}
