package com.iglooit.core.expr.iface.command.request;

import com.iglooit.commons.iface.type.UUID;
import com.iglooit.core.base.iface.command.Request;
import com.iglooit.core.expr.iface.command.response.ExprReadResponse;
import com.iglooit.core.expr.iface.domain.Expr;

public class ExprReadRequest extends Request<ExprReadResponse>
{
    private UUID<Expr> expressionId;

    public ExprReadRequest()
    {
    }

    public ExprReadRequest(UUID<Expr> expressionId)
    {
        this.expressionId = expressionId;
    }

    public UUID<Expr> getExpressionId()
    {
        return expressionId;
    }
}
