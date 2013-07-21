package com.iglooit.core.command.server.handlers;

import com.clarity.core.base.iface.command.request.GetNavigationCustomLogoRequest;
import com.clarity.core.base.iface.command.response.GetNavigationCustomLogoResponse;
import com.clarity.core.security.iface.access.domain.FunctionPrivileges.SystemPrivilege;
import com.clarity.core.security.iface.access.domain.PrivilegeConst;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class GetNavigationCustomLogoRequestHandler
        extends RequestHandler<GetNavigationCustomLogoRequest, GetNavigationCustomLogoResponse>
{
    private static final PrivilegeConst[] PRIVILEGES = new PrivilegeConst[]{ SystemPrivilege.LOGIN };

    @Resource(name = "navigationCustomLogoUrl")
    private String logoURL;

    @Resource(name = "navigationCustomLogoWidth")
    private String logoWidth;

    @Override
    public GetNavigationCustomLogoResponse handleRequest(GetNavigationCustomLogoRequest request)
    {
        return new GetNavigationCustomLogoResponse(logoURL, logoWidth);
    }

    @Override
    public PrivilegeConst[] getRequiredPrivileges()
    {
        return PRIVILEGES;
    }
}
