package com.iglooit.core.command.server.handlers;

import com.clarity.commons.iface.domain.DateCacheEntry;
import com.clarity.commons.iface.domain.SystemDateProvider;
import com.clarity.core.base.iface.command.request.GetSystemDateRequest;
import com.clarity.core.base.iface.command.response.GetSystemDateResponse;
import com.clarity.core.security.iface.access.domain.FunctionPrivileges.SystemPrivilege;
import com.clarity.core.security.iface.access.domain.PrivilegeConst;
import org.springframework.stereotype.Component;

@Component
public class GetSystemDateRequestHandler extends RequestHandler<GetSystemDateRequest, GetSystemDateResponse>
{
    private static final PrivilegeConst[] PRIVILEGES = {SystemPrivilege.LOGIN};

    @Override
    public GetSystemDateResponse handleRequest(GetSystemDateRequest request)
    {
        DateCacheEntry dateCacheEntry = SystemDateProvider.getDate(request.getLocalDate(), null);
        return new GetSystemDateResponse(dateCacheEntry);
    }

    @Override
    public PrivilegeConst[] getRequiredPrivileges()
    {
        return PRIVILEGES;
    }
}
