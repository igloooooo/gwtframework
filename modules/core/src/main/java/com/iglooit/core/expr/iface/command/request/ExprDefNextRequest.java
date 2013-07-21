package com.iglooit.core.expr.iface.command.request;

import com.iglooit.core.base.iface.command.Request;
import com.iglooit.core.expr.iface.command.response.ExprDefNextResponse;
import com.iglooit.core.expr.iface.domain.ExprDef;
import com.iglooit.core.expr.iface.domain.RuleType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExprDefNextRequest extends Request<ExprDefNextResponse>
{
    private RuleType ruleType;
    private List<ExprDef> exprDefs;

    //GWT ONLY
    public ExprDefNextRequest()
    {
    }

    public ExprDefNextRequest(RuleType ruleType, ExprDef exprDef)
    {
        this.ruleType = ruleType;
        this.exprDefs = Arrays.asList(exprDef);
    }

    public ExprDefNextRequest(RuleType ruleType, List<ExprDef> exprDefs)
    {
        this.ruleType = ruleType;
        this.exprDefs = exprDefs;
    }

    public ExprDefNextRequest(RuleType ruleType)
    {
        this.ruleType = ruleType;
        this.exprDefs = new ArrayList<ExprDef>();
    }

    public List<ExprDef> getExprDefs()
    {
        return exprDefs;
    }

    public RuleType getRuleType()
    {
        return ruleType;
    }
}
