package com.iglooit.core.base.iface.expression;

import java.util.List;

public class SimpleLogicalOperation implements ISimpleExpression
{
    private SimpleLogicalOperationOperators operator;
    private ISimpleExpression[] operands;

    public SimpleLogicalOperation(SimpleLogicalOperationOperators operator, ISimpleExpression... operands)
    {
        if (operands.length < 1)
        {
            throw new IllegalArgumentException(Integer.toString(operands.length));
        }
        this.operator = operator;
        this.operands = operands;
    }

    public SimpleLogicalOperation(SimpleLogicalOperationOperators operator, List<ISimpleExpression> operands)
    {
        this(operator, operands.toArray(new ISimpleExpression[operands.size()]));
    }

    public String toString()
    {
        StringBuilder sb = new StringBuilder();

        boolean first = true;
        sb.append("(");
        for (ISimpleExpression operand : operands)
        {
            if (!first)
            {
                sb.append(") ");
                sb.append(operator);
                sb.append(" (");
            }
            sb.append(operand.toString());
            first = false;
        }
        sb.append(")");

        return sb.toString();
    }

    public SimpleLogicalOperationOperators getOperator()
    {
        return operator;
    }

    public ISimpleExpression[] getOperands()
    {
        return operands;
    }
}
