package com.iglooit.core.base.iface.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

public class GFunc implements IsSerializable, Serializable
{
    private String name;
    private String label;
    private boolean enabled;


    public GFunc()
    {
    }

    public GFunc(String name, String label, boolean enabled)
    {
        this.name = name;
        this.label = label;
        this.enabled = enabled;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public boolean isEnabled()
    {
        return enabled;
    }

    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }
}
