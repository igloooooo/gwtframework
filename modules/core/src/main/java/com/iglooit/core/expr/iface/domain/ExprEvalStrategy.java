package com.iglooit.core.expr.iface.domain;

import java.util.Map;

public interface ExprEvalStrategy
{
    ExprResult eval(Expr expression, Map<String, Object> expressionContext);
}
