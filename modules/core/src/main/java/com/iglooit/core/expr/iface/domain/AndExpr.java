package com.iglooit.core.expr.iface.domain;

import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.UUID;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Map;

@Entity
@DiscriminatorValue("AndExpr")
public class AndExpr extends Expr<AndExpr>
{
    private static final boolean CAN_HAVE_CHILDREN = true;

    public AndExpr()
    {
    }

    public AndExpr(UUID exprId)
    {
        super(exprId);
    }

    @Override
    public ExprResult eval(ExprEvalStrategyFactory exprEvalStrategyFactory,
                           Map<String, Object> expressionContext)
    {
        ensureCorrectNumberofChildren(2, Boolean.class, Boolean.class);

        ExprResult lhs = getChildren().get(0).eval(exprEvalStrategyFactory, expressionContext);
        ExprResult rhs = getChildren().get(1).eval(exprEvalStrategyFactory, expressionContext);

        boolean result = (Boolean)lhs.getResult() && (Boolean)rhs.getResult();

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
        return new AndExprMeta(this);
    }

    public boolean canHaveChildren()
    {
        return CAN_HAVE_CHILDREN;
    }

    @Override
    public ExprDef getExprDef()
    {
        return new AndExprDef();
    }
}
