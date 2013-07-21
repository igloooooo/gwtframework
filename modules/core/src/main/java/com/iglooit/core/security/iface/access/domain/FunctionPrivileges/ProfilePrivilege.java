package com.iglooit.core.security.iface.access.domain.FunctionPrivileges;

import com.clarity.core.security.iface.access.domain.PrivilegeConst;

public enum ProfilePrivilege implements PrivilegeConst
{
    ADMIN("profile.admin");

    private final String name;

    private ProfilePrivilege(String name)
    {
        this.name = name;
    }

    @Override
    public String getPrivilegeString()
    {
        return name;
    }
}
