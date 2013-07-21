package com.iglooit.core.expr.iface.domain;

import com.iglooit.commons.iface.domain.Validator;
import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.UUID;

import java.util.Arrays;
import java.util.List;

public class AttributeValueExprDef extends ExprDef
{
    public AttributeValueExprDef()
    {
        super(ExprDefType.ATTRIB_VALUE, "Attribute Value");
    }

    @Override
    public Expr instantiate(UUID expressionId)
    {
        return new AttributeValueExpr(expressionId);
    }

    @Override
    public List<Validator> doGetValidators()
    {
        return Arrays.asList();
    }

    @Override
    protected Meta createMetaDelegate()
    {
        return new AttributeValueExprDefMeta(this);
    }
}
