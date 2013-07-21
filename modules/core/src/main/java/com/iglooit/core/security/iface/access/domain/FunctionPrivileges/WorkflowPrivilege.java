package com.iglooit.core.security.iface.access.domain.FunctionPrivileges;

import com.clarity.core.security.iface.access.domain.PrivilegeConst;

public enum WorkflowPrivilege implements PrivilegeConst
{
    VIEW ("workflow.view"),
    START ("workflow.start"),
    PAUSE ("workflow.pause"),
    CREATE ("workflow.create"),
    UPDATE ("workflow.update"),
    CANCEL ("workflow.cancel");

    private final String name;

    WorkflowPrivilege(String name)
    {
        this.name = name;
    }

    @Override
    public String getPrivilegeString()
    {
        return name;
    }
}
