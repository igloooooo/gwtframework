package com.iglooit.core.command.server.handlers;

import com.clarity.core.base.iface.command.ClarityModulesReadListRequest;
import com.clarity.core.base.iface.command.ListResponse;
import com.clarity.core.oss.server.OSSHome;
import com.clarity.core.security.iface.access.domain.FunctionPrivileges.SystemPrivilege;
import com.clarity.core.security.iface.access.domain.PrivilegeConst;
import org.springframework.stereotype.Component;

@Component
public class ClarityModulesReadListRequestHandler extends RequestHandler<ClarityModulesReadListRequest,
    ListResponse<String>>
{
    private static final PrivilegeConst[] PRIVILEGES = {SystemPrivilege.LOGIN};

    @Override
    public ListResponse<String> handleRequest(ClarityModulesReadListRequest request)
    {
        return new ListResponse<String>(OSSHome.getInstance().fetchSupportedClarityModulesAsString());
    }

    @Override
    public PrivilegeConst[] getRequiredPrivileges()
    {
        return PRIVILEGES;
    }
}
