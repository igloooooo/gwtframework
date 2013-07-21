package com.iglooit.core.expr.iface.domain;

import com.iglooit.commons.iface.domain.Validator;
import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.UUID;

import java.util.List;

public class NamedAttributeValueExprDef extends ExprDef
{
    public NamedAttributeValueExprDef()
    {
        super(ExprDefType.NAMED_ATTRIB_VALUE, "Named Attribute Value");
    }

    @Override
    public Expr instantiate(UUID expressionId)
    {
        return new NamedAttributeValueExpr(expressionId);
    }

    @Override
    public List<Validator> doGetValidators()
    {
        return null;
    }

    @Override
    protected Meta createMetaDelegate()
    {
        return new NamedAttributeValueExprDefMeta(this);
    }
}
