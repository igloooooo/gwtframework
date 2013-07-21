package com.iglooit.core.base.iface.dto;

import com.clarity.commons.iface.type.UUID;

import java.util.List;

public class GString extends GObject
{
    private String value;
    private String[] potentialValues;
    private String workflowId;

    public GString()
    {

    }

    public GString(UUID id, boolean dirty, String name, String type, String value, boolean editable, boolean mandatory)
    {
        super(id, dirty, name, type, editable, mandatory);
        this.value = value;
    }

    public GString(UUID id, boolean dirty, String name, String type, String value, boolean editable, boolean mandatory
        , String description)
    {
        super(id, dirty, name, type, editable, mandatory, description);
        this.value = value;
    }


    public void setPotentialValues(String[] potentialValues)
    {
        this.potentialValues = potentialValues;
    }

    public void setPotentialValues(List potentialValues)
    {
        this.potentialValues = new String[potentialValues.size()];
        for (int i = 0; i < this.potentialValues.length; i++)
        {
            final Object value = potentialValues.get(i);
            this.potentialValues[i] = value == null ? "" : value.toString();
        }
    }

    public String[] getPotentialValues()
    {
        return potentialValues == null ? new String[0] : potentialValues;
    }

    public String getValue()
    {
        return value;
    }

    public void doSetValue(Object value)
    {
        this.value = (String)value;
    }

    public String getWorkflowId()
    {
        return workflowId;
    }

    public void setWorkflowId(String workflowId)
    {
        this.workflowId = workflowId;
    }
}
