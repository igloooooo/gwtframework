package com.iglooit.core.expr.server.domain;

import com.iglooit.core.expr.iface.domain.*;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class EmailValueExprEvalStrategy implements ExprEvalStrategy
{
    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[-a-z0-9~!$%^&*_=+}{\\'?]+(\\.[-a-z0-9~!$%^&*_=+}{\\'?]+)*@([a-z0-9_][-a-z0-9_]*" +
                "(\\.[-a-z0-9_]+)*\\.(aero|arpa|biz|com|coop|edu|gov|info|int|mil|museum|name|net|org|pro|travel|mobi|" +
                "[a-z][a-z])|([0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}))(:[0-9]{1,5})?$",
                Pattern.CASE_INSENSITIVE);

    @Resource
    private ExprEvalStrategyFactory exprEvalStrategyFactory;

    @PostConstruct
    private void init()
    {
        exprEvalStrategyFactory.add(EmailExpr.class, this);
    }

    @Override
    public ExprResult eval(Expr expr, Map<String, Object> expressionContext)
    {
        expr.ensureCorrectNumberofChildren(1, String.class);

        String inputValue = (String)expr.evalSubExpr(0, expressionContext, exprEvalStrategyFactory).getResult();

        Matcher matcher = EMAIL_PATTERN.matcher(inputValue);

        return new ExprResult(matcher.matches());
    }
}