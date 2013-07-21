package com.iglooit.core.security.iface.access.domain.FunctionPrivileges;

import com.clarity.core.security.iface.access.domain.PrivilegeConst;

public enum EntityDefPrivilege implements PrivilegeConst
{
    CREATE ("entitydef.create");

    private final String name;

    private EntityDefPrivilege(String name)
    {
        this.name = name;
    }

    @Override
    public String getPrivilegeString()
    {
        return name;
    }
}
