package com.iglooit.core.expr.iface.command.response;

import com.iglooit.core.base.iface.command.DomainEntityResponse;
import com.iglooit.core.expr.iface.domain.Expr;

public class ExprReadResponse extends DomainEntityResponse<Expr>
{
    public ExprReadResponse()
    {
    }

    public ExprReadResponse(Expr domainEntity)
    {
        super(domainEntity);
    }
}
