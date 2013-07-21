package com.iglooit.core.base.iface.expression;

public class SimpleBinaryComparison implements ISimpleExpression
{
    private String fieldName;
    private SimpleBinaryComparisonOperators operator;
    private String value;

    public SimpleBinaryComparison(String fieldName, SimpleBinaryComparisonOperators operator, String value)
    {
        this.fieldName = fieldName;
        this.operator = operator;
        this.value = value;
    }

    public String toString()
    {
        return fieldName + " " +  operator + " \"" + value + "\"";
    }

    public String getFieldName()
    {
        return fieldName;
    }

    public SimpleBinaryComparisonOperators getOperator()
    {
        return operator;
    }

    public String getValue()
    {
        return value;
    }
}
