package com.iglooit.core.base.client.widget.form;

import com.extjs.gxt.ui.client.widget.form.Field;
import com.extjs.gxt.ui.client.widget.form.MultiField;

public class GMultiField<T extends Field> extends MultiField
{
    private final T fieldWithLabel;

    public GMultiField(T fieldWithLabel)
    {
        this.fieldWithLabel = fieldWithLabel;
        add(fieldWithLabel);
    }

    public Field getMainField()
    {
        return fieldWithLabel;
    }

    @Override
    public String getFieldLabel()
    {
        return fieldWithLabel.getFieldLabel();
    }

    @Override
    public String getLabelSeparator()
    {
        return fieldWithLabel.getLabelSeparator();
    }

    @Override
    public void setFieldLabel(String fieldLabel)
    {
        fieldWithLabel.setFieldLabel(fieldLabel);
    }

    @Override
    public void setLabelSeparator(String labelSeparator)
    {
        fieldWithLabel.setLabelSeparator(labelSeparator);
    }

    @Override
    public String getEmptyText()
    {
        return fieldWithLabel.getEmptyText();
    }

    @Override
    public void setEmptyText(String emptyText)
    {
        fieldWithLabel.setEmptyText(emptyText);
    }
}
