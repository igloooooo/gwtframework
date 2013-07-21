package com.iglooit.core.expr.iface.domain;

import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.UUID;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.List;
import java.util.Map;

//todo at: Rename this to UserExpr (and def)
@Entity
@DiscriminatorValue("UserListExpr")
public class UserListExpr extends Expr<UserListExpr>
{

    private static final boolean CAN_HAVE_CHILDREN = false;

    public UserListExpr()
    {
    }

    public UserListExpr(UUID expressionId)
    {
        super(expressionId);
    }

    @Override
    public ExprResult eval(ExprEvalStrategyFactory exprEvalStrategyFactory, Map<String, Object> expressionContext)
    {
        //todo at: return a list of users.  At the moment use the string value they selected
        return new ExprResult(getStringValue());
    }

    @Override
    public Class getReturnType()
    {
        return List.class;
    }

    @Override
    protected Meta createMetaDelegate()
    {
        return new UserListExprMeta(this);
    }

    public boolean canHaveChildren()
    {
        return CAN_HAVE_CHILDREN;
    }

    @Override
    public ExprDef getExprDef()
    {
        return new UserListExprDef();
    }
}
