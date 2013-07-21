package com.iglooit.core.expr.iface.domain;

import com.iglooit.commons.iface.domain.Validator;
import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.UUID;

import java.util.Arrays;
import java.util.List;

public class IPv6ExprDef extends ExprDef
{
    public IPv6ExprDef()
    {
        super(ExprDefType.IPV6_VALUE, "Is IPv6 address");
    }


    @Override
    public Expr instantiate(UUID expressionId)
    {
        return new IPv6Expr(expressionId);
    }

    @Override
    public List<Validator> doGetValidators()
    {
        return Arrays.asList();
    }

    @Override
    protected Meta createMetaDelegate()
    {
        return new IPv6ExprDefMeta(this);
    }
}