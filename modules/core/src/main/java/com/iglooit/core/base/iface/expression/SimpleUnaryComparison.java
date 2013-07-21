package com.iglooit.core.base.iface.expression;

public class SimpleUnaryComparison implements ISimpleExpression
{
    private String fieldName;
    private SimpleUnaryComparisonOperators operator;

    public SimpleUnaryComparison(String fieldName, SimpleUnaryComparisonOperators operator)
    {
        this.fieldName = fieldName;
        this.operator = operator;
    }

    public String toString()
    {
        return fieldName + " " +  operator;
    }

    public String getFieldName()
    {
        return fieldName;
    }

    public SimpleUnaryComparisonOperators getOperator()
    {
        return operator;
    }
}
