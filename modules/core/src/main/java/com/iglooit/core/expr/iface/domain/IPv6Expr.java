package com.iglooit.core.expr.iface.domain;

import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.UUID;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Map;

@Entity
@DiscriminatorValue("IPv6Expr")
public class IPv6Expr extends Expr<IPv6Expr>
{
    private static final boolean CAN_HAVE_CHILDREN = true;


    public IPv6Expr()
    {
        super();
    }

    public IPv6Expr(UUID exprId)
    {
        super(exprId);
    }

    @Override
    public ExprResult eval(ExprEvalStrategyFactory exprEvalStrategyFactory, Map<String, Object> expressionContext)
    {
        return exprEvalStrategyFactory.getExprEvalStrategy(this.getClass()).eval(this, expressionContext);
    }

    @Override
    public Class getReturnType()
    {
        return Boolean.class;
    }

    @Override
    protected Meta createMetaDelegate()
    {
        return new IPv6ExprMeta(this);
    }

    public boolean canHaveChildren()
    {
        return CAN_HAVE_CHILDREN;
    }

    @Override
    public ExprDef getExprDef()
    {
        return new IPv6ExprDef();
    }
}