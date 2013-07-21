package com.iglooit.core.expr.server.command;

import com.iglooit.core.command.server.handlers.RequestHandler;
import com.iglooit.core.expr.iface.command.request.ExprDefNextRequest;
import com.iglooit.core.expr.iface.command.response.ExprDefNextResponse;
import com.iglooit.core.expr.iface.domain.ExprDef;
import com.iglooit.core.expr.server.domain.ExprHome;
import com.iglooit.core.security.iface.access.domain.FunctionPrivileges.SystemPrivilege;
import com.iglooit.core.security.iface.access.domain.PrivilegeConst;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class ExprDefNextRequestHandler extends RequestHandler<ExprDefNextRequest, ExprDefNextResponse>
{
    private static final PrivilegeConst[] PRIVILEGES = {SystemPrivilege.LOGIN};

    @Resource
    private ExprHome exprHome;

    public PrivilegeConst[] getRequiredPrivileges()
    {
        return PRIVILEGES;
    }

    @Override
    public ExprDefNextResponse handleRequest(ExprDefNextRequest request)
    {
        return new ExprDefNextResponse(exprHome.getNextLegalExprDefsForPartialExpression(request.getRuleType(),
            request.getExprDefs().toArray(new ExprDef[0])));
    }
}
