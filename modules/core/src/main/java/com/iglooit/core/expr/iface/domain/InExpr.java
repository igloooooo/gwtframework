package com.iglooit.core.expr.iface.domain;

import com.clarity.commons.iface.domain.meta.Meta;
import com.clarity.commons.iface.type.UUID;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;
import java.util.Map;

@Entity
@DiscriminatorValue("InExpr")
public class InExpr extends Expr<InExpr>
{

    private static final boolean CAN_HAVE_CHILDREN = true;

    public InExpr()
    {
    }

    public InExpr(UUID expressionId)
    {
        super(expressionId);
    }

    @Override
    public ExprResult eval(ExprEvalStrategyFactory exprEvalStrategyFactory, Map<String, Object> expressionContext)
    {
        ensureCorrectNumberofChildren(2, Object.class, List.class);

        ExprResult lhs = evalSubExpr(0, expressionContext, exprEvalStrategyFactory);
        ExprResult rhs = evalSubExpr(1, expressionContext, exprEvalStrategyFactory);

        if (rhs.getResult() instanceof List && lhs.getResult() instanceof List)
        {
            for (Object rhsOption : (List)rhs.getResult())
            {
                if (((List)lhs.getResult()).contains(rhsOption))
                {
                    return new ExprResult(Boolean.TRUE, lhs, rhs);
                }
            }
        }
        return new ExprResult(Boolean.FALSE, lhs, rhs);
    }

    @Override
    public Class getReturnType()
    {
        return Boolean.class;
    }

    @Override
    protected Meta createMetaDelegate()
    {
        return new InExprMeta(this);
    }

    public boolean canHaveChildren()
    {
        return CAN_HAVE_CHILDREN;
    }

    @Override
    public ExprDef getExprDef()
    {
        return new InExprDef();
    }
}
