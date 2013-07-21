package com.iglooit.core.security.iface.access.response;

import com.clarity.core.base.iface.command.DomainEntityListResponse;
import com.clarity.core.security.iface.access.domain.Privilege;

import java.util.List;

public class PrivilegeReadListResponse extends DomainEntityListResponse<Privilege>
{
    public PrivilegeReadListResponse()
    {
    }

    public PrivilegeReadListResponse(List<Privilege> privileges)
    {
        super(privileges);
    }
}
