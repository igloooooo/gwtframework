package com.iglooit.core.expr.iface.domain;

import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.UUID;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Map;

@Entity
@DiscriminatorValue("IPv4Expr")
public class IPv4Expr extends Expr<IPv4Expr>
{
    private static final boolean CAN_HAVE_CHILDREN = true;


    public IPv4Expr()
    {
        super();
    }

    public IPv4Expr(UUID exprId)
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
        return new IPv4ExprMeta(this);
    }

    public boolean canHaveChildren()
    {
        return CAN_HAVE_CHILDREN;
    }

    @Override
    public ExprDef getExprDef()
    {
        return new IPv4ExprDef();
    }
}