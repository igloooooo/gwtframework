package com.iglooit.core.security.iface.access.domain.FunctionPrivileges;

import com.clarity.core.security.iface.access.domain.PrivilegeConst;

public enum FaultManagerPrivileges implements PrivilegeConst
{
    FM_BACKGROUND("FM_BACKGROUND"),
    FM_CREATE("FM_CREATE"),
    FM_MAINTENANCE("FM_MAINTENANCE"),
    FM_REPORTING("FM_REPORTING"),
    FM_SUPERVISOR("FM_SUPERVISOR"),
    AS_DATA_MANAGER("AS_DATA_MANAGER"),
    //followinig role is valid only on non-security clarity release
    FM_DATA_MANAGER("FM_DATA_MANAGER");

    private final String name;

    private FaultManagerPrivileges(String name)
    {
        this.name = name;
    }

    @Override
    public String getPrivilegeString()
    {
        return name;
    }

}
