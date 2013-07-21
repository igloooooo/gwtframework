package com.iglooit.core.command.server.handlers;

import com.clarity.core.base.iface.command.request.GetUserManagementStatusRequest;
import com.clarity.core.base.iface.command.response.GetUserManagementStatusResponse;
import com.clarity.core.config.server.domain.ConfigHome;
import com.clarity.core.security.iface.access.domain.FunctionPrivileges.SystemPrivilege;
import com.clarity.core.security.iface.access.domain.PrivilegeConst;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class GetUserManagementStatusRequestHandler
    extends RequestHandler<GetUserManagementStatusRequest, GetUserManagementStatusResponse>
{
    private static final PrivilegeConst[] PRIVILEGES = {SystemPrivilege.LOGIN};

    @Resource
    private ConfigHome configHome;

    @Override
    public GetUserManagementStatusResponse handleRequest(GetUserManagementStatusRequest request)
    {
        return new GetUserManagementStatusResponse(configHome.doWeManageUserPasswords());
    }

    @Override
    public PrivilegeConst[] getRequiredPrivileges()
    {
        return PRIVILEGES;
    }
}
