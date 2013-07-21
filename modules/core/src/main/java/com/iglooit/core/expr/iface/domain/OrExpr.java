package com.iglooit.core.expr.iface.domain;

import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.UUID;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Map;

@Entity
@DiscriminatorValue("OrExpr")
public class OrExpr extends Expr<OrExpr>
{

    private static final boolean CAN_HAVE_CHILDREN = true;

    public OrExpr()
    {
    }

    public OrExpr(UUID expressionId)
    {
        super(expressionId);
    }

    @Override
    public ExprResult eval(ExprEvalStrategyFactory exprEvalStrategyFactory, Map<String, Object> expressionContext)
    {
        ensureCorrectNumberofChildren(2, Boolean.class, Boolean.class);

        ExprResult lhs = evalSubExpr(0, expressionContext, exprEvalStrategyFactory);
        ExprResult rhs = evalSubExpr(1, expressionContext, exprEvalStrategyFactory);

        boolean result = (Boolean)lhs.getResult() || (Boolean)rhs.getResult();

        return new ExprResult(result, lhs, rhs);
    }

    @Override
    public Class getReturnType()
    {
        return Boolean.class;
    }

    @Override
    protected Meta createMetaDelegate()
    {
        return new OrExprMeta(this);
    }

    public boolean canHaveChildren()
    {
        return CAN_HAVE_CHILDREN;
    }

    @Override
    public ExprDef getExprDef()
    {
        return new OrExprDef();
    }
}
