package com.iglooit.core.base.client.model;

import com.extjs.gxt.ui.client.data.BaseModelData;

public class EntityModelData extends BaseModelData
{
    public static final String ENTITY = "entity";
    public static final String DISPLAY_VALUE = "displayValue";

    public EntityModelData(IModelDataEntity entity)
    {
        set(ENTITY, entity);
        set(DISPLAY_VALUE, entity.getDisplayString());
    }

    public IModelDataEntity getEntity()
    {
        return (IModelDataEntity)get(ENTITY);
    }

    @Override
    public String toString()
    {
        return getEntity().toString();
    }
}
