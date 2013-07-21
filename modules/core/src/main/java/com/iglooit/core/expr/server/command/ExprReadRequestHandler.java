package com.iglooit.core.expr.server.command;

import com.iglooit.core.command.server.handlers.RequestHandler;
import com.iglooit.core.expr.iface.command.request.ExprReadRequest;
import com.iglooit.core.expr.iface.command.response.ExprReadResponse;
import com.iglooit.core.expr.server.domain.ExprHome;
import com.iglooit.core.security.iface.access.domain.FunctionPrivileges.SystemPrivilege;
import com.iglooit.core.security.iface.access.domain.PrivilegeConst;

import javax.annotation.Resource;

public class ExprReadRequestHandler extends RequestHandler<ExprReadRequest, ExprReadResponse>
{
    private static final PrivilegeConst[] PRIVILEGES = {SystemPrivilege.LOGIN};

    @Resource
    private ExprHome exprHome;

    public PrivilegeConst[] getRequiredPrivileges()
    {
        return PRIVILEGES;
    }

    @Override
    public ExprReadResponse handleRequest(ExprReadRequest request)
    {
        return new ExprReadResponse(exprHome.loadExpr(request.getExpressionId()));
    }
}
