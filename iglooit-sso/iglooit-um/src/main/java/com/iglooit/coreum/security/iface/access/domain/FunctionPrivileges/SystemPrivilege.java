package com.iglooit.coreum.security.iface.access.domain.FunctionPrivileges;

import com.iglooit.coreum.security.iface.access.domain.PrivilegeConst;

public enum SystemPrivilege implements PrivilegeConst
{
    LOGIN("system.login"),
    ADMIN("system.admin"),
    /**
     * Special privilege that if present on it's own, means no privileges login or privileges are required.
     */
    NONE("system.none");

    private final String name;

    private SystemPrivilege(String name)
    {
        this.name = name;
    }

    @Override
    public String getPrivilegeString()
    {
        return name;
    }
}
