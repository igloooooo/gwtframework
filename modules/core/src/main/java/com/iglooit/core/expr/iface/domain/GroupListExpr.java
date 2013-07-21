package com.iglooit.core.expr.iface.domain;

import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.UUID;

import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Entity
public class GroupListExpr extends Expr<GroupListExpr>
{

    private static final boolean CAN_HAVE_CHILDREN = false;

    public GroupListExpr()
    {
    }

    public GroupListExpr(UUID expressionId)
    {

        super(expressionId);
    }

    @Override
    public ExprResult eval(ExprEvalStrategyFactory exprEvalStrategyFactory, Map<String, Object> expressionContext)
    {
        ArrayList listOfGroups = new ArrayList();
        String[] values = getStringValue().split(ExprDef.LIST_VALUE_SEPARATOR);

        for (String value : values)
        {
            if (!value.isEmpty())
            {
                listOfGroups.add(value);
            }
        }

        return new ExprResult(listOfGroups);
    }

    @Override
    public Class getReturnType()
    {
        return List.class;
    }

    @Override
    protected Meta createMetaDelegate()
    {
        return new GroupListExprMeta(this);
    }

    public boolean canHaveChildren()
    {
        return CAN_HAVE_CHILDREN;
    }

    @Override
    public ExprDef getExprDef()
    {
        return new GroupListExprDef();
    }
}
