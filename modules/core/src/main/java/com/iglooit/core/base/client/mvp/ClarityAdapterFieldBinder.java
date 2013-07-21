package com.iglooit.core.base.client.mvp;

import com.clarity.core.base.client.widget.form.ClarityAdapterField;
import com.clarity.core.base.iface.domain.ValidatableMeta;

public class ClarityAdapterFieldBinder extends LabelledBinder
{
    private final ValidatableMeta validatableMeta;

    public ClarityAdapterFieldBinder(ClarityAdapterField field, ValidatableMeta validatableMeta, String metaFieldName)
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
