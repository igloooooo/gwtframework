package com.iglooit.core.expr.server.domain;

import com.iglooit.core.expr.iface.domain.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class RegexMatchesExprEvalStrategy implements ExprEvalStrategy
{
    @Resource
    private ExprEvalStrategyFactory exprEvalStrategyFactory;

    @PostConstruct
    private void init()
    {
        exprEvalStrategyFactory.add(RegexMatchesExpr.class, this);
    }

    @Override
    public ExprResult eval(Expr expr, Map<String, Object> expressionContext)
    {
        expr.ensureCorrectNumberofChildren(2, String.class, String.class);

        String inputValue = (String)expr.evalSubExpr(0, expressionContext, exprEvalStrategyFactory).getResult();
        String regex = (String)expr.evalSubExpr(1, expressionContext, exprEvalStrategyFactory).getResult();

        if (regex == null || inputValue == null)
            return new ExprResult(Boolean.FALSE);

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(inputValue);

        if (matcher.matches())
        {
            return new ExprResult(Boolean.TRUE);
        }
        else
        {
            return new ExprResult(Boolean.FALSE);
        }
    }
}



