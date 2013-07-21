package com.iglooit.core.base.client.mvp;

import com.clarity.core.base.iface.domain.HasLabelledValue;
import com.clarity.commons.iface.domain.meta.LabelledMeta;

public class ImmutableBinder<X> extends LabelledBinder<X>
{
    public ImmutableBinder(HasLabelledValue<X> value, LabelledMeta meta, String metaFieldName)
    {
        super(value, meta, metaFieldName);
        value.setValue((X)meta.get(metaFieldName));
    }

    public boolean isModified()
    {
        return false;
    }

    public void unbind()
    {
        getTracker().removeBinder(this);
    }

    public void undoModifications()
    {

    }
}
