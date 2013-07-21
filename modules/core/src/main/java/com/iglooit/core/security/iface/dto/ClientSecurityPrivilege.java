package com.iglooit.core.security.iface.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

public class ClientSecurityPrivilege implements IsSerializable, Serializable
{
    public String getName()
    {
        return name;
    }

    private String name;

    public ClientSecurityPrivilege(String name)
    {
        this.name = name;
    }

    public ClientSecurityPrivilege()
    {
    }
}
