package com.iglooit.core.security.iface.access.domain.FunctionPrivileges;

import com.clarity.core.security.iface.access.domain.PrivilegeConst;

public enum CatalogPrivilege implements PrivilegeConst
{
    READ("catalog.read"),
    CREATE("catalog.create"),
    UPDATE("catalog.update");

    private final String name;

    private CatalogPrivilege(String name)
    {
        this.name = name;
    }

    @Override
    public String getPrivilegeString()
    {
        return name;
    }
}
