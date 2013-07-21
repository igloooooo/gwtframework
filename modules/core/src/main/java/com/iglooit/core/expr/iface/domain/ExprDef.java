package com.iglooit.core.expr.iface.domain;

import com.iglooit.commons.iface.type.AppX;
import com.iglooit.commons.iface.type.UUID;
import com.iglooit.core.base.iface.domain.DomainEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ExprDef extends DomainEntity
{
    public static final String LIST_VALUE_SEPARATOR = ",";

    private Map<ExprDefType, ExprDef> children = new HashMap<ExprDefType, ExprDef>();
    private ExprDefType type;
    private String displayName;

    //todo jm: refactor
    // move displayName to a front end mapping object of ExprDefType to displayName 
    protected ExprDef(ExprDefType type, String displayName)
    {
        this.type = type;
        this.displayName = displayName;
    }

    //for meta's only
    public Map<ExprDefType, ExprDef> getChildren()
    {
        return children;
    }

    //for meta's only
    public void setChildren(Map<ExprDefType, ExprDef> children)
    {
        this.children = children;
    }

    //for meta's only.
    public void setType(ExprDefType type)
    {
        throw new AppX("Cannot set final value");
    }

    public abstract Expr instantiate(UUID expressionId);

    public ExprDefType getType()
    {
        return type;
    }

    public void addExpressionCombination(ExprDef... exprDefPath)
    {
        ExprDef node = this;
        for (ExprDef exprDef : exprDefPath)
        {
            node = node.findOrAdd(exprDef);
        }
    }

    private ExprDef findOrAdd(ExprDef exprDef)
    {
        ExprDef result = children.get(exprDef.type);
        if (result == null)
        {
            result = exprDef;
            children.put(exprDef.type, result);
        }
        return result;

    }

    public List<ExprDef> getNextLegalExprDefs(ExprDef[] exprDefs)
    {
        if (exprDefs == null || exprDefs.length == 0)
            return new ArrayList<ExprDef>(this.children.values());

        if (children == null || children.size() == 0)
        {
            return new ArrayList<ExprDef>();
        }
        ExprDef x = children.get(exprDefs[0].type);

        ExprDef[] xs = new ExprDef[exprDefs.length - 1];
        System.arraycopy(exprDefs, 1, xs, 0, exprDefs.length - 1);

        return x.getNextLegalExprDefs(xs);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof ExprDef)) return false;

        ExprDef exprDef = (ExprDef)o;

        if (type != exprDef.type) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        return type != null ? type.hashCode() : 0;
    }

    public String getDisplayName()
    {
        return displayName;
    }

    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }
}
