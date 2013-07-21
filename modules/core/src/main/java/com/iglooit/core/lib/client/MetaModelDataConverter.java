package com.iglooit.core.lib.client;

import com.clarity.commons.iface.domain.meta.Meta;
import com.clarity.core.lib.iface.TypeConverter;
import com.extjs.gxt.ui.client.data.ModelComparer;

/**
 * Created by IntelliJ IDEA. User: ftsang Date: 7/06/12 Time: 4:03 PM
 */
public class MetaModelDataConverter implements TypeConverter<Meta, MetaModelData>
{
    private static MetaModelDataConverter instance;

    private ModelComparer comparer;

    static
    {
        instance = new MetaModelDataConverter(null);
    }

    public static MetaModelDataConverter getInstance()
    {
        return instance;
    }

    public MetaModelDataConverter(ModelComparer comparer)
    {
        this.comparer = comparer;
    }

    public ModelComparer getComparer()
    {
        return comparer;
    }

    @Override
    public MetaModelData convert(Meta m)
    {
        MetaModelData model = new MetaModelData(m);
        model.setComparer(comparer);
        return model;
    }
}
