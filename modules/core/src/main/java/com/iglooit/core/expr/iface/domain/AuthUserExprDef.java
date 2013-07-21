package com.iglooit.core.expr.iface.domain;

import com.iglooit.commons.iface.domain.Validator;
import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.UUID;

import java.util.Arrays;
import java.util.List;

public class AuthUserExprDef extends ExprDef
{
    public static final String AUTHENTICATED_USER_KEY = "AUTHENTICATED_USER";

    public AuthUserExprDef()
    {
        super(ExprDefType.AUTH_USER, "User");
    }

    @Override
    public Expr instantiate(UUID expressionId)
    {
        return new AuthUserExpr(expressionId);
    }

    @Override
    public List<Validator> doGetValidators()
    {
        return Arrays.asList();
    }

    @Override
    protected Meta createMetaDelegate()
    {
        return new AuthUserExprDefMeta(this);
    }
}
