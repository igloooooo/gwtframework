package com.iglooit.core.expr.iface.domain;

import com.iglooit.commons.iface.domain.Validator;
import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.UUID;

import java.util.Arrays;
import java.util.List;

public class NumberExprDef extends ExprDef
{
    public NumberExprDef()
    {
        super(ExprDefType.NUMBER_VALUE, "Is number of length");
    }


    @Override
    public Expr instantiate(UUID expressionId)
    {
        return new NumberExpr(expressionId);
    }

    @Override
    public List<Validator> doGetValidators()
    {
        return Arrays.asList();
    }

    @Override
    protected Meta createMetaDelegate()
    {
        return new NumberExprDefMeta(this);
    }
}