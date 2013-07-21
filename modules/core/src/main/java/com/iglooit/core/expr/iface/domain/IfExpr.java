package com.iglooit.core.expr.iface.domain;

import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.commons.iface.type.AppX;
import com.iglooit.commons.iface.type.UUID;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Map;

@Entity
@DiscriminatorValue("IfExpr")
public class IfExpr extends Expr<IfExpr>
{
    public IfExpr()
    {
    }

    public IfExpr(UUID<Expr> exprId)
    {
        super(exprId);
    }

    @Override
    public ExprDef getExprDef()
    {
        return new IfExprDef();
    }

    @Override
    public ExprResult eval(ExprEvalStrategyFactory exprEvalStrategyFactory, Map<String, Object> expressionContext)
    {
        if (getChildren() == null || !(getChildren().size() == 2 || getChildren().size() == 3))
        {
            throw new AppX("IF_EXPR must have 2 or 3 children : " + this.toString());
        }

        //Evaluate our LHS (an equals expr)

        if (getChildren().get(0).getExprDef().getType().equals(ExprDefType.EQUALS))
        {
            Expr lhs = getChildren().get(0);

            ExprResult result = lhs.eval(exprEvalStrategyFactory, expressionContext);

            if (result.getResult().getClass().equals(Boolean.class))
            {
                Boolean boolResult = (Boolean)result.getResult();
                if (boolResult)
                {
                    ExprResult childResult = getChildren().get(1).eval(exprEvalStrategyFactory, expressionContext);
                    return new ExprResult(childResult.getResult().toString(), childResult);
                }
                else if (getChildren().size() == 3)
                {

                    Expr ifChild = getChildren().get(2);

                    //todo at: Handle other RHS leaf types (String etc)
                    if (!ifChild.getExprDef().getType().equals(ExprDefType.IF_EXPR))
                    {
                        throw new AppX("RHS of a 3-child IF_EXPR must be another IF_EXPR: " + ifChild);
                    }

                    return getChildren().get(2).eval(exprEvalStrategyFactory, expressionContext);
                }
                else
                {
                    return new ExprResult(null, new ExprResult[0]);
                }

            }
            else
            {
                throw new AppX("LHS of an IF_EXPR must evaluate to a boolean");
            }
        }
        else
        {
            throw new AppX("Left hand side of IF_EXPR must be an EQUALS, instead it is: " + getChildren().get(0));
        }
    }

    @Override
    public boolean canHaveChildren()
    {
        return true;
    }


    @Override
    public Class getReturnType()
    {
        return Object.class;
    }

    @Override
    protected Meta createMetaDelegate()
    {
        return new IfExprMeta(this);
    }
}
