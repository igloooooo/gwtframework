package com.iglooit.core.expr.iface.domain;

import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.UUID;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Map;

@Entity
@DiscriminatorValue("NotEqualsExpr")
public class NotEqualsExpr extends Expr<NotEqualsExpr>
{
    private static final boolean CAN_HAVE_CHILDREN = true;

    public NotEqualsExpr()
    {
    }

    public NotEqualsExpr(UUID exprId)
    {
        super(exprId);
    }

    @Override
    public ExprResult eval(ExprEvalStrategyFactory exprEvalStrategyFactory,
                           Map<String, Object> expressionContext)
    {
        ensureCorrectNumberofChildren(2);

        Expr lhs = getChildren().get(0);
        Expr rhs = getChildren().get(1);

        ExprResult lhsVal = lhs.eval(exprEvalStrategyFactory, expressionContext);
        ExprResult rhsVal = rhs.eval(exprEvalStrategyFactory, expressionContext);

        boolean result = (lhsVal.getResult() == null && rhsVal.getResult() != null) ||        //one not null or
            (lhsVal.getResult() != null
                && !lhsVal.getResult().equals(rhsVal.getResult()));    //invoke equals (in a null safe way)

        return new ExprResult(result, lhsVal, rhsVal);

    }

    @Override
    public Class getReturnType()
    {
        return Boolean.class;
    }

    @Override
    protected Meta createMetaDelegate()
    {
        return new NotEqualsExprMeta(this);
    }

    public boolean canHaveChildren()
    {
        return CAN_HAVE_CHILDREN;
    }

    @Override
    public ExprDef getExprDef()
    {
        return new NotEqualsExprDef();
    }
}
