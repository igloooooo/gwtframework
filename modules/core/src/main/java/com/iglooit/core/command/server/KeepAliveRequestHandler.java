package com.iglooit.core.command.server;

import com.iglooit.core.account.iface.domain.UserRole;
import com.iglooit.core.base.iface.command.request.KeepAliveRequest;
import com.iglooit.core.base.iface.command.response.VoidResponse;
import com.iglooit.core.command.server.handlers.RequestHandler;
import com.iglooit.core.iface.SecurityService;
import com.iglooit.core.security.iface.access.domain.FunctionPrivileges.SystemPrivilege;
import com.iglooit.core.security.iface.access.domain.PrivilegeConst;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class KeepAliveRequestHandler extends RequestHandler<KeepAliveRequest, VoidResponse>
{
    private static final Log LOG = LogFactory.getLog(KeepAliveRequestHandler.class);
    private static final PrivilegeConst[] PRIVILEGES = {SystemPrivilege.LOGIN};

    @Resource
    private SecurityService securityService;

    public VoidResponse handleRequest(KeepAliveRequest request)
    {
        UserRole currentUser = securityService.getActiveUserRole().value();
        if (LOG.isDebugEnabled()) LOG.debug("KeepAlive for user: " + currentUser.getUsername());
        return new VoidResponse();
    }

    public PrivilegeConst[] getRequiredPrivileges()
    {
        return PRIVILEGES;
    }
}
