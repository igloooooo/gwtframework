package com.iglooit.core.expr.iface.domain;

import com.iglooit.commons.iface.domain.Validator;
import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.UUID;

import java.util.Arrays;
import java.util.List;

public class InExprDef extends ExprDef
{
    public InExprDef()
    {
        super(ExprDefType.IN, "In");
    }

    @Override
    public Expr instantiate(UUID expressionId)
    {
        return new InExpr(expressionId);
    }

    @Override
    public List<Validator> doGetValidators()
    {
        return Arrays.asList();
    }

    @Override
    protected Meta createMetaDelegate()
    {
        return new InExprDefMeta(this);
    }
}
