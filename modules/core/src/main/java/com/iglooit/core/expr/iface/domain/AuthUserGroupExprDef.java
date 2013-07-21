package com.iglooit.core.expr.iface.domain;

import com.iglooit.commons.iface.domain.Validator;
import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.UUID;

import java.util.Arrays;
import java.util.List;

public class AuthUserGroupExprDef extends ExprDef
{

    public AuthUserGroupExprDef()
    {
         super(ExprDefType.AUTH_USER_GROUPS, "Group");
    }

    @Override
    public Expr instantiate(UUID expressionId)
    {
        return new AuthUserGroupExpr(expressionId);
    }

    @Override
    public List<Validator> doGetValidators()
    {
        return Arrays.asList();
    }

    @Override
    protected Meta createMetaDelegate()
    {
        return new AuthUserGroupExprDefMeta(this);
    }
}
