package com.iglooit.core.expr.iface.command.response;

import com.iglooit.core.base.iface.command.Response;
import com.iglooit.core.expr.iface.domain.ExprDef;

import java.util.List;

public class ExprDefNextResponse extends Response
{

    private List<ExprDef> exprDefs;

    //GWT ONLY
    public ExprDefNextResponse()
    {
        
    }

    public ExprDefNextResponse(List<ExprDef> exprDefs)
    {
        this.exprDefs = exprDefs;
    }

    public List<ExprDef> getExprDefs()
    {
        return exprDefs;
    }
}
