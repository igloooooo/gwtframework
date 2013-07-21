package com.iglooit.core.lib.client;

import com.clarity.commons.iface.domain.meta.Meta;
import com.clarity.commons.iface.type.AppX;
import com.extjs.gxt.ui.client.data.ModelData;

import java.util.*;

public class MultipleMetaModelData implements ModelData
{
    private Map<String, ModelData> demux = new HashMap<String, ModelData>();
    private List<Meta> metas = new ArrayList<Meta>();
    private Set<String> localProperties = new TreeSet<String>();
    public static final String SEPARATOR = ".";
    private static final String SEPARATOR_REGEX = "\\.";

    public MultipleMetaModelData()
    {
    }

    public <M extends Meta> void addDomainEntity(M meta,
                                                 String propertyPrefix,
                                                 String... propertyNames)
    {
        metas.add(meta);
        MetaModelData<M> demd = new MetaModelData<M>(meta, propertyNames);
        demux.put(propertyPrefix, demd);
        for (String propertyName : propertyNames)
            localProperties.add(propertyPrefix + SEPARATOR + propertyName);
    }

    public List<Meta> getMetas()
    {
        return metas;
    }

    private String getMetaProperty(String query)
    {
        return query.split(SEPARATOR_REGEX)[1];
    }

    private ModelData getMetaClass(String query)
    {
        ModelData md = demux.get(query.split(SEPARATOR_REGEX)[0]);
        if (md == null)
            throw new AppX("Cannot find class for query string: " + query);
        return md;
    }


    public <X> X get(String s)
    {
        return (X)getMetaClass(s).get(getMetaProperty(s));
    }

    public Map<String, Object> getProperties()
    {
        Map<String, Object> allProperties = new HashMap<String, Object>();
        for (String lp : localProperties)
            allProperties.put(lp, this.get(lp));
        return allProperties;
    }

    public Collection<String> getPropertyNames()
    {
        return localProperties;
    }

    public <X> X remove(String s)
    {
        return (X)getMetaClass(s).remove(getMetaProperty(s));
    }

    public <X> X set(String s, X x)
    {
        return getMetaClass(s).set(getMetaProperty(s), x);
    }
}
