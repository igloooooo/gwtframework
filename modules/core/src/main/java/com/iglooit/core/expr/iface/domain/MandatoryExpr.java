package com.iglooit.core.expr.iface.domain;

import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.UUID;

import javax.persistence.Entity;
import java.util.Map;

@Entity
public class MandatoryExpr extends Expr<MandatoryExpr>
{
    public MandatoryExpr()
    {
    }

    public MandatoryExpr(UUID expressionId)
    {
        super(expressionId);
    }

    @Override
    public ExprResult eval(ExprEvalStrategyFactory exprEvalStrategyFactory, Map<String, Object> expressionContext)
    {
        ensureCorrectNumberofChildren(1, Boolean.class);
        ExprResult lhs = getChildren().get(0).eval(exprEvalStrategyFactory, expressionContext);

        //todo jm: potentially allow for non-string mandatory attributes that have an empty toString
        boolean result = lhs.getResult() != null && lhs.getResult().toString().length() != 0;
        return new ExprResult(result, lhs);
    }

    @Override
    public boolean canHaveChildren()
    {
        return true;
    }

    @Override
    public Class getReturnType()
    {
        return Boolean.class;
    }

    @Override
    protected Meta createMetaDelegate()
    {
        return new MandatoryExprMeta(this);
    }

    @Override
    public ExprDef getExprDef()
    {
        return new MandatoryExprDef();
    }
}
