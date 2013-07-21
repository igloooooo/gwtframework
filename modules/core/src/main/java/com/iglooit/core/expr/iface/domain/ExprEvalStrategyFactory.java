package com.iglooit.core.expr.iface.domain;

public interface ExprEvalStrategyFactory
{
    ExprEvalStrategy getExprEvalStrategy(Class clazz);
    void add(Class expr, ExprEvalStrategy strategy);
    
}
