package com.iglooit.core.expr.iface.domain;

import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.UUID;

import javax.persistence.Entity;
import java.util.List;
import java.util.Map;

@Entity
public class NotInExpr extends Expr<NotInExpr>
{

    private static final boolean CAN_HAVE_CHILDREN = true;

    public NotInExpr()
    {
    }

    public NotInExpr(UUID expressionId)
    {
        super(expressionId);
    }

    @Override
    public ExprResult eval(ExprEvalStrategyFactory exprEvalStrategyFactory, Map<String, Object> expressionContext)
    {
        ensureCorrectNumberofChildren(2, Object.class, List.class);
        
        ExprResult lhs = evalSubExpr(0, expressionContext, exprEvalStrategyFactory);
        ExprResult rhs = evalSubExpr(1, expressionContext, exprEvalStrategyFactory);

        if (rhs.getResult() instanceof List)
        {
            for (Object rhsOption : (List)rhs.getResult())
            {
                if (lhs.equals(rhsOption))
                {
                    return new ExprResult(Boolean.FALSE, lhs, rhs);
                }
            }
        }
        return new ExprResult(Boolean.TRUE, lhs, rhs);
    }

    @Override
    public Class getReturnType()
    {
        return Boolean.class;
    }

    @Override
    protected Meta createMetaDelegate()
    {
        return new NotInExprMeta(this);
    }

    public boolean canHaveChildren()
    {
        return CAN_HAVE_CHILDREN;
    }

    @Override
    public ExprDef getExprDef()
    {
        return new NotInExprDef();
    }
}
