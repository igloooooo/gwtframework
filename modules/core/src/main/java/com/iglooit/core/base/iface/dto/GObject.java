package com.iglooit.core.base.iface.dto;

import com.clarity.commons.iface.type.UUID;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

public abstract class GObject implements IsSerializable, Serializable
{
    private UUID id;
    private boolean dirty;
    private String name;
    private String type;
    private boolean editable;
    private boolean mandatory;
    private String description;

    /**
     * don't use this - required for GWT serialisation
     */
    public GObject()
    {

    }

    protected GObject(UUID id, boolean dirty, String name, String type, boolean editable, boolean mandatory)
    {
        this.id = id;
        this.dirty = dirty;
        this.name = name;
        this.type = type;
        this.editable = editable;
        this.mandatory = mandatory;
    }

    protected GObject(UUID id, boolean dirty, String name, String type, boolean editable, boolean mandatory,
                      String description)
    {
        this(id, dirty, name, type, editable, mandatory);
        setDescription(description);
    }

    public abstract Object getValue();

    public final void setValue(Object value)
    {
        doSetValue(value);
        dirty = true;
    }

    public abstract void doSetValue(Object value);

    public UUID getId()
    {
        return id;
    }

    public void setId(UUID id)
    {
        this.id = id;
    }

    public boolean isDirty()
    {
        return dirty;
    }

    public void setDirty(boolean dirty)
    {
        this.dirty = dirty;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public boolean isEditable()
    {
        return editable;
    }

    public void setEditable(boolean editable)
    {
        this.editable = editable;
    }

    public boolean isMandatory()
    {
        return mandatory;
    }

    public void setMandatory(boolean mandatory)
    {
        this.mandatory = mandatory;
    }
}
