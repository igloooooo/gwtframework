package com.iglooit.core.expr.iface.domain;

import com.iglooit.commons.iface.domain.Validator;
import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.UUID;

import java.util.ArrayList;
import java.util.List;

public class OwningUserExprDef extends ExprDef
{

    public OwningUserExprDef()
    {
        super(ExprDefType.OWNINGUSER, "Is Product Owner");
    }

    @Override
    public Expr instantiate(UUID expressionId)
    {
        return new OwningUserExpr(expressionId);
    }

    @Override
    public List<Validator> doGetValidators()
    {
        return new ArrayList<Validator>();
    }

    @Override
    protected Meta createMetaDelegate()
    {
        return new OwningUserExprDefMeta(this);
    }
}
