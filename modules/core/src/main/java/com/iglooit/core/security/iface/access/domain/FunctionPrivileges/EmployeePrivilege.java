package com.iglooit.core.security.iface.access.domain.FunctionPrivileges;

import com.clarity.core.security.iface.access.domain.PrivilegeConst;

public enum EmployeePrivilege implements PrivilegeConst
{
    ADMIN ("employee.admin");

    private final String name;

    private EmployeePrivilege(String name)
    {
        this.name = name;
    }

    @Override
    public String getPrivilegeString()
    {
        return name;
    }

}
