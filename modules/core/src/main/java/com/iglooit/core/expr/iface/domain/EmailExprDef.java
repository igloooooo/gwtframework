package com.iglooit.core.expr.iface.domain;

import com.iglooit.commons.iface.domain.Validator;
import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.UUID;

import java.util.Arrays;
import java.util.List;

public class EmailExprDef extends ExprDef
{
    public EmailExprDef()
    {
        super(ExprDefType.EMAIL_VALUE, "Is email address");
    }


    @Override
    public Expr instantiate(UUID expressionId)
    {
        return new EmailExpr(expressionId);
    }

    @Override
    public List<Validator> doGetValidators()
    {
        return Arrays.asList();
    }

    @Override
    protected Meta createMetaDelegate()
    {
        return new EmailExprDefMeta(this);
    }
}