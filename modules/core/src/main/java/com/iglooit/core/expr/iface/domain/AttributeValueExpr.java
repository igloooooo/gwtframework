package com.iglooit.core.expr.iface.domain;

import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.AppX;
import com.iglooit.commons.iface.type.UUID;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Map;

@Entity
@DiscriminatorValue(value = "ATTRIBUTE_VALUE")
public class AttributeValueExpr extends Expr<AttributeValueExpr>
{
    public static final String INPUT_ATTRIBUTE_VALUE_KEY = "INPUT_VALUE_KEY";
    private static final boolean CAN_HAVE_CHILDREN = false;

    //GWT Only
    public AttributeValueExpr()
    {
        super();
    }

    public AttributeValueExpr(UUID exprId)
    {
        super(exprId);
    }

    @Override
    public ExprResult eval(ExprEvalStrategyFactory exprEvalStrategyFactory, Map<String, Object> expressionContext)
    {
        ensureCorrectNumberofChildren(0);

        if (!expressionContext.containsKey(AttributeValueExpr.INPUT_ATTRIBUTE_VALUE_KEY))
        {
            throw new AppX("Expression not provided with required input value " +
                AttributeValueExpr.INPUT_ATTRIBUTE_VALUE_KEY + ": " + this);
        }

        return new ExprResult(expressionContext.get(AttributeValueExpr.INPUT_ATTRIBUTE_VALUE_KEY));
    }

    @Override
    public Class getReturnType()
    {
        return String.class;
    }

    @Override
    protected Meta createMetaDelegate()
    {
        return new AttributeValueExprMeta(this);
    }

    public boolean canHaveChildren()
    {
        return CAN_HAVE_CHILDREN;
    }

    @Override
    public ExprDef getExprDef()
    {
        return new AttributeValueExprDef();
    }
}
