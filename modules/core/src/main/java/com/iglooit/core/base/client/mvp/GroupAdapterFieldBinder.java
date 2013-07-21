package com.iglooit.core.base.client.mvp;

import com.clarity.core.base.iface.domain.ValidatableMeta;

public class GroupAdapterFieldBinder extends LabelledBinder
{
    private final ValidatableMeta validatableMeta;

    public GroupAdapterFieldBinder(GroupAdapterField field, ValidatableMeta validatableMeta, String metaFieldName)
    {
        super(field, validatableMeta, metaFieldName);
        this.validatableMeta = validatableMeta;
    }

    @Override
    public boolean isModified()
    {
        return false;
    }

    @Override
    public void unbind()
    {

    }

    @Override
    public String getFieldLabel()
    {
        return validatableMeta.getDefaultFieldLabel(getMetaFieldName());
    }

    @Override
    public void undoModifications()
    {

    }
}
