package com.iglooit.core.expr.iface.domain;

import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.AppX;
import com.iglooit.commons.iface.type.UUID;
import com.iglooit.core.account.iface.domain.Individual;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Map;

@Entity
@DiscriminatorValue("AuthUserExpr")
public class AuthUserExpr extends Expr<AuthUserExpr>
{
    private static final boolean CAN_HAVE_CHILDREN = false;

    public AuthUserExpr()
    {
        super();
    }

    public AuthUserExpr(UUID exprId)
    {
        super(exprId);
    }

    @Override
    public ExprResult eval(ExprEvalStrategyFactory exprEvalStrategyFactory, Map<String, Object> expressionContext)
    {
        ensureCorrectNumberofChildren(0);

        if (!expressionContext.containsKey(AuthUserExprDef.AUTHENTICATED_USER_KEY))
            throw new AppX("Expression context does not contain " +
                AuthUserExprDef.AUTHENTICATED_USER_KEY + ": " + this);

        //todo at: Migrate to use the id rather than the name
        return new ExprResult(
            ((Individual)expressionContext.get(AuthUserExprDef.AUTHENTICATED_USER_KEY)).getName());
    }

    @Override
    public Class getReturnType()
    {
        return String.class;
    }

    @Override
    protected Meta createMetaDelegate()
    {
        return new AuthUserExprMeta(this);
    }

    public boolean canHaveChildren()
    {
        return CAN_HAVE_CHILDREN;
    }

    @Override
    public ExprDef getExprDef()
    {
        return new AuthUserExprDef();
    }
}
