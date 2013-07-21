package com.iglooit.core.security.server.handlers;

import com.clarity.core.account.server.domain.IndividualHome;
import com.clarity.core.command.server.handlers.RequestHandler;
import com.clarity.core.iface.SecurityService;
import com.clarity.core.lib.server.security.impl.SecurityHome;
import com.clarity.core.security.iface.access.domain.FunctionPrivileges.SystemPrivilege;
import com.clarity.core.security.iface.access.domain.Privilege;
import com.clarity.core.security.iface.access.domain.PrivilegeConst;
import com.clarity.core.security.iface.access.request.PrivilegeReadListRequest;
import com.clarity.core.security.iface.access.response.PrivilegeReadListResponse;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

@Component
public class PrivilegeReadListRequestHandler extends RequestHandler<PrivilegeReadListRequest, PrivilegeReadListResponse>
{
    private static final PrivilegeConst[] PRIVILEGES = new PrivilegeConst[]{ SystemPrivilege.LOGIN };

    @Resource
    private SecurityHome securityHome;
    @Resource
    private IndividualHome individualHome;
    @Resource
    private SecurityService securityService;

    @Override
    public PrivilegeReadListResponse handleRequest(PrivilegeReadListRequest request)
    {
        //we can assume that the user has a session at this point
        final List<Privilege> privileges = securityHome.getPrivileges();
        return new PrivilegeReadListResponse(privileges);
    }

    @Override
    public PrivilegeConst[] getRequiredPrivileges()
    {
        return PRIVILEGES;
    }
}