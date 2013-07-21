package com.iglooit.core.security.iface.access.domain.FunctionPrivileges;

import com.clarity.core.security.iface.access.domain.PrivilegeConst;

public enum VisualiserPrivilege implements PrivilegeConst
{
    VISUALISER_USER("visualiser.user");

    private final String name;

    private VisualiserPrivilege(String name)
    {
        this.name = name;
    }

    @Override
    public String getPrivilegeString()
    {
        return name;
    }

}
