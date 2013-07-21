package com.iglooit.core.expr.iface.domain;

import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.UUID;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Map;

@Entity
@DiscriminatorValue("StringValueExpr")
public class StringValueExpr extends Expr<StringValueExpr>
{
    private static final boolean CAN_HAVE_CHILDREN = false;

    public StringValueExpr()
    {
        super();
    }

    public StringValueExpr(UUID exprId, String value)
    {
        super(exprId, value);
    }

    public StringValueExpr(UUID exprId)
    {
        super(exprId);
    }

    @Override
    public ExprResult eval(ExprEvalStrategyFactory exprEvalStrategyFactory, Map<String, Object> expressionContext)
    {
        ensureCorrectNumberofChildren(0);
        return new ExprResult(getStringValue());
    }

    @Override
    public Class getReturnType()
    {
        return String.class;
    }

    @Override
    protected Meta createMetaDelegate()
    {
        return new StringValueExprMeta(this);
    }

    public boolean canHaveChildren()
    {
        return CAN_HAVE_CHILDREN;
    }

    @Override
    public ExprDef getExprDef()
    {
        return new StringValueExprDef();
    }
}
