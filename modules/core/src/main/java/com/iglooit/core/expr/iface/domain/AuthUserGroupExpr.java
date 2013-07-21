package com.iglooit.core.expr.iface.domain;

import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.UUID;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;
import java.util.Map;

@Entity
@DiscriminatorValue(value = "AuthUserGroupExpr")
public class AuthUserGroupExpr extends Expr<AuthUserGroupExpr>
{

    private static final boolean CAN_HAVE_CHILDREN = false;

    public AuthUserGroupExpr()
    {
    }

    public AuthUserGroupExpr(UUID expressionId)
    {
        super(expressionId);
    }


    @Override
    public ExprResult eval(ExprEvalStrategyFactory exprEvalStrategyFactory, Map<String, Object> expressionContext)
    {
        return exprEvalStrategyFactory.getExprEvalStrategy(this.getClass()).eval(this, expressionContext);
    }

    @Override
    public Class getReturnType()
    {
        return List.class;
    }

    @Override
    protected Meta createMetaDelegate()
    {
        return new AuthUserGroupExprMeta(this);
    }

    public boolean canHaveChildren()
    {
        return CAN_HAVE_CHILDREN;
    }

    @Override
    public ExprDef getExprDef()
    {
        return new AuthUserGroupExprDef();
    }
}
