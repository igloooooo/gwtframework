package com.iglooit.core.expr.iface.domain;

import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.UUID;

import javax.persistence.Entity;
import java.util.Map;

@Entity
public class OwningUserExpr extends Expr<OwningUserExpr>
{

    private static final boolean CAN_HAVE_CHILDREN = true;

    public OwningUserExpr()
    {
        super();
    }

    public OwningUserExpr(UUID<Expr> exprId)
    {
        super(exprId);
    }

    @Override
    protected Meta createMetaDelegate()
    {
        return new OwningUserExprMeta(this);
    }

    @Override
    public ExprDef getExprDef()
    {
        return new OwningUserExprDef();
    }

    @Override
    public ExprResult eval(ExprEvalStrategyFactory exprEvalStrategyFactory, Map<String, Object> expressionContext)
    {
        ensureCorrectNumberofChildren(1);

        //todo jm: check if the current user created/owns the entity that this attribute on.
        return new ExprResult(Boolean.TRUE);
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
}
