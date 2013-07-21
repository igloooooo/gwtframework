package com.iglooit.core.expr.iface.domain;

import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.UUID;

import javax.persistence.Entity;
import java.util.Map;

@Entity
public class BooleanValueExpr extends Expr<BooleanValueExpr>
{

    public BooleanValueExpr()
    {
    }

    public BooleanValueExpr(UUID expressionId)
    {
        super(expressionId);
    }

    @Override
    public ExprResult eval(ExprEvalStrategyFactory exprEvalStrategyFactory, Map<String, Object> expressionContext)
    {
        ensureCorrectNumberofChildren(0);
        //todo jm:
        //expr do not support non-string values at the moment, so use a new Boolean(StringValue)
        return new ExprResult(Boolean.valueOf(getStringValue()));
    }

    @Override
    public boolean canHaveChildren()
    {
        return false;
    }

    @Override
    public Class getReturnType()
    {
        return Boolean.class;
    }

    @Override
    protected Meta createMetaDelegate()
    {
        return new BooleanValueExprMeta(this);
    }

    @Override
    public ExprDef getExprDef()
    {
        return new BooleanValueExprDef();
    }
}
