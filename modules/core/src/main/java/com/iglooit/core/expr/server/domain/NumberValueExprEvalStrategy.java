package com.iglooit.core.expr.server.domain;

import com.iglooit.commons.iface.util.StringUtil;
import com.iglooit.core.expr.iface.domain.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

@Component
public class NumberValueExprEvalStrategy implements ExprEvalStrategy
{
    @Resource
    private ExprEvalStrategyFactory exprEvalStrategyFactory;

    @PostConstruct
    private void init()
    {
        exprEvalStrategyFactory.add(NumberExpr.class, this);
    }

    @Override
    public ExprResult eval(Expr expr, Map<String, Object> expressionContext)
    {
        expr.ensureCorrectNumberofChildren(2, String.class);
        final String inputValue = (String)expr.evalSubExpr(0, expressionContext, exprEvalStrategyFactory).getResult();
        final String lengthValue = (String)expr.evalSubExpr(1, expressionContext, exprEvalStrategyFactory).getResult();
        final int length = Integer.parseInt(lengthValue);
        return new ExprResult(inputValue.length() == length && StringUtil.isNumeric(inputValue));
    }
}