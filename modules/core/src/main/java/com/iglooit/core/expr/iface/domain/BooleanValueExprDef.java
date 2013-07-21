package com.iglooit.core.expr.iface.domain;

import com.iglooit.commons.iface.domain.Validator;
import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.UUID;

import java.util.Arrays;
import java.util.List;

public class BooleanValueExprDef extends ExprDef
{

    public BooleanValueExprDef()
    {
        super(ExprDefType.BOOLEAN_VALUE, "T/F");
    }

    @Override
    public Expr instantiate(UUID expressionId)
    {
        return new BooleanValueExpr(expressionId);
    }

    @Override
    public List<Validator> doGetValidators()
    {
        return Arrays.asList();
    }

    @Override
    protected Meta createMetaDelegate()
    {
        return new BooleanValueExprDefMeta(this);
    }
}
