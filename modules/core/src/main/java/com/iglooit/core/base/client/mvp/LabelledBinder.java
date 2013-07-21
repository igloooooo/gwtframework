package com.iglooit.core.base.client.mvp;

import com.clarity.core.base.iface.domain.HasLabelledValue;
import com.clarity.commons.iface.domain.meta.LabelledMeta;

public abstract class LabelledBinder<X> extends Binder<X>
{
    private String fieldLabel;

    protected LabelledBinder(HasLabelledValue<X> labelledValue, LabelledMeta labelledMeta, String metaFieldName)
    {
        super(metaFieldName);
        setWidgetFieldLabelAndHint(labelledValue, labelledMeta);
    }

    protected void setWidgetFieldLabelAndHint(HasLabelledValue<X> labelledValue, LabelledMeta labelledMeta)
    {
        if (labelledValue.getFieldLabel().length() > 0)
            fieldLabel = labelledValue.getFieldLabel();
        else
            fieldLabel = labelledMeta.getDefaultFieldLabel(getMetaFieldName());

        labelledValue.setFieldLabel(fieldLabel);

        if (labelledValue.getUsageHint().isNone())
            labelledValue.setUsageHint(labelledMeta.getDefaultFieldUsageHint(getMetaFieldName()));
    }

    public String getFieldLabel()
    {
        return fieldLabel;
    }
}
