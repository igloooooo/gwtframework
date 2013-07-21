package com.iglooit.core.expr.iface.domain;

import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.AppX;
import com.iglooit.commons.iface.type.UUID;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Map;

@Entity
@DiscriminatorValue("NOT_REGEX")
public class NotRegexMatchesExpr extends Expr<NotRegexMatchesExpr>
{
    private static final boolean CAN_HAVE_CHILDREN = true;

    public NotRegexMatchesExpr()
    {
        super();
    }

    public NotRegexMatchesExpr(UUID exprId)
    {
        super(exprId);
    }

    @Override
    public ExprResult eval(ExprEvalStrategyFactory exprEvalStrategyFactory, Map<String, Object> expressionContext)
    {
        ExprEvalStrategy evalStrategy = exprEvalStrategyFactory.getExprEvalStrategy(RegexMatchesExpr.class);
        ExprResult result = evalStrategy.eval(this, expressionContext);
        Object rawResult = result.getResult();
        if (rawResult instanceof Boolean)
        {
            Boolean previousExprResult = (Boolean)rawResult;
            return new ExprResult(!previousExprResult, result.getContext());
        }
        else
            throw new AppX("Expected RegexMatchesExpr to return a boolean");
    }

    @Override
    public Class getReturnType()
    {
        return Boolean.class;
    }

    @Override
    protected Meta createMetaDelegate()
    {
        return new NotRegexMatchesExprMeta(this);
    }

    public boolean canHaveChildren()
    {
        return CAN_HAVE_CHILDREN;
    }

    @Override
    public ExprDef getExprDef()
    {
        return new NotRegexMatchesExprDef();
    }
}
