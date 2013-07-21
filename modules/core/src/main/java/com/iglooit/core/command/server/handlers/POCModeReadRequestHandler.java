package com.iglooit.core.command.server.handlers;

import com.clarity.core.base.iface.command.request.POCModeReadRequest;
import com.clarity.core.base.iface.command.response.BooleanResponse;
import com.clarity.core.security.iface.access.domain.FunctionPrivileges.SystemPrivilege;
import com.clarity.core.security.iface.access.domain.PrivilegeConst;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class POCModeReadRequestHandler extends RequestHandler<POCModeReadRequest, BooleanResponse>
{
    private static final PrivilegeConst[] PRIVILEGES = {SystemPrivilege.LOGIN};

    @Resource
    private Boolean enablePOCmode;

    @Override
    public BooleanResponse handleRequest(POCModeReadRequest request)
    {
        if (enablePOCmode == null)
            enablePOCmode = Boolean.FALSE;
        return new BooleanResponse(enablePOCmode);
    }

    @Override
    public PrivilegeConst[] getRequiredPrivileges()
    {
        return PRIVILEGES;
    }
}
