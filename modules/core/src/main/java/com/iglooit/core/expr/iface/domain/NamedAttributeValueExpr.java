package com.iglooit.core.expr.iface.domain;

import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.AppX;
import com.iglooit.commons.iface.type.UUID;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Map;

@Entity
@DiscriminatorValue("NamedAttributeValueExpr")
public class NamedAttributeValueExpr extends Expr<NamedAttributeValueExpr>
{
    public NamedAttributeValueExpr()
    {
    }

    public NamedAttributeValueExpr(UUID<Expr> exprId)
    {
        super(exprId);
    }

    public NamedAttributeValueExpr(UUID<Expr> exprId, String stringValue)
    {
        super(exprId, stringValue);
    }

    @Override
    public ExprDef getExprDef()
    {
        return new NamedAttributeValueExprDef();
    }

    @Override
    public ExprResult eval(ExprEvalStrategyFactory exprEvalStrategyFactory, Map<String, Object> expressionContext)
    {
        ensureCorrectNumberofChildren(0);

        String attributeName = getStringValue();
        Map<String, String> attributeMap = (Map<String, String>)expressionContext.get("ATTRIBUTE_VALUE_MAP");

        if (attributeMap == null)
        {
            throw new AppX("Could not find attribute map in expression context" + this.toString());
        }

        return new ExprResult(attributeMap.get(attributeName));
    }

    @Override
    public boolean canHaveChildren()
    {
        return false;
    }

    @Override
    public Class getReturnType()
    {
        return String.class;
    }

    @Override
    protected Meta createMetaDelegate()
    {
        return new NamedAttributeValueExprMeta(this);
    }
}
