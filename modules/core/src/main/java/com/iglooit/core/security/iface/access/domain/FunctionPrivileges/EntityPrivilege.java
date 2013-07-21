package com.iglooit.core.security.iface.access.domain.FunctionPrivileges;

import com.clarity.core.security.iface.access.domain.PrivilegeConst;

public enum EntityPrivilege implements PrivilegeConst
{
    CREATE ("entity.create");

    private final String name;

    private EntityPrivilege(String name)
    {
        this.name = name;
    }

    @Override
    public String getPrivilegeString()
    {
        return name;
    }
}
