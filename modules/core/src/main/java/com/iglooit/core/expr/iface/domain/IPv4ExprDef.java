package com.iglooit.core.expr.iface.domain;

import com.iglooit.commons.iface.domain.Validator;
import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.UUID;

import java.util.Arrays;
import java.util.List;

public class IPv4ExprDef extends ExprDef
{
    public IPv4ExprDef()
    {
        super(ExprDefType.IPV4_VALUE, "Is IPv4 address");
    }


    @Override
    public Expr instantiate(UUID expressionId)
    {
        return new IPv4Expr(expressionId);
    }

    @Override
    public List<Validator> doGetValidators()
    {
        return Arrays.asList();
    }

    @Override
    protected Meta createMetaDelegate()
    {
        return new IPv4ExprDefMeta(this);
    }
}