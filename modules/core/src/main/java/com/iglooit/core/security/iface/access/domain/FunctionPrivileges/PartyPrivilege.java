package com.iglooit.core.security.iface.access.domain.FunctionPrivileges;

import com.clarity.core.security.iface.access.domain.PrivilegeConst;

public enum PartyPrivilege implements PrivilegeConst
{
    CREATE ("party.create"),
    READ ("party.read"),
    UPDATE ("party.update"),
    DELETE ("party.delete");

    private final String name;

    private PartyPrivilege(String name)
    {
        this.name = name;
    }

    @Override
    public String getPrivilegeString()
    {
        return name;
    }
}
