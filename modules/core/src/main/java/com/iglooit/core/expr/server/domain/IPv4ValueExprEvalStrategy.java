package com.iglooit.core.expr.server.domain;

import com.iglooit.core.expr.iface.domain.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class IPv4ValueExprEvalStrategy implements ExprEvalStrategy
{
    private static final Pattern IPV4_PATTERN =
        Pattern.compile("\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
                "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b");

    @Resource
    private ExprEvalStrategyFactory exprEvalStrategyFactory;

    @PostConstruct
    private void init()
    {
        exprEvalStrategyFactory.add(IPv4Expr.class, this);
    }

    @Override
    public ExprResult eval(Expr expr, Map<String, Object> expressionContext)
    {
        expr.ensureCorrectNumberofChildren(1, String.class);

        String inputValue = (String)expr.evalSubExpr(0, expressionContext, exprEvalStrategyFactory).getResult();

        if (inputValue == null || inputValue.length() == 0)
            return new ExprResult(Boolean.FALSE);

        Matcher matcher = IPV4_PATTERN.matcher(inputValue);

        return new ExprResult(matcher.matches());
    }
}

