package com.iglooit.core.expr.server.domain;

import com.iglooit.commons.iface.type.UUID;
import com.iglooit.core.expr.iface.domain.*;
import com.iglooit.core.iface.SecurityService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
public class ExprHome
{
    @PersistenceContext(unitName = "oss")
    private EntityManager em;

    @Resource
    private SecurityService securityService;

    @Resource
    private ExprEvalStrategyFactory exprEvalStrategyFactory;

    private Map<RuleType, ExprDef> expressionRules = new HashMap<RuleType, ExprDef>();

    public ExprHome()
    {
        ExprDef validationTree = buildValidationTree();
        expressionRules.put(RuleType.VALIDATION, validationTree);

        ExprDef accessTree = buildAccessTree();
        expressionRules.put(RuleType.ACCESS, accessTree);
        expressionRules.put(RuleType.MODIFICATION, accessTree);
        expressionRules.put(RuleType.RATING, buildRatingTree());

    }

    private static ExprDef buildRatingTree()
    {
        ExprDef ratingTree = new RootExprDef();


        ratingTree.addExpressionCombination(new NamedAttributeValueExprDef(), new EqualsExprDef(),
            new StringValueExprDef(),
            new StringValueExprDef());


        return ratingTree;
    }

    private static ExprDef buildAccessTree()
    {
        ExprDef accessTree = new RootExprDef();

        // TODO: uncomment the following two lines when UserListExprDef is implemented
//        accessTree.addExpressionCombination(new AuthUserExprDef(), new EqualsExprDef(), new UserListExprDef());
//        accessTree.addExpressionCombination(new AuthUserExprDef(), new NotEqualsExprDef(), new UserListExprDef());
        accessTree.addExpressionCombination(new AuthUserExprDef(), new OwningUserExprDef());

        accessTree.addExpressionCombination(new AuthUserGroupExprDef(), new InExprDef(), new GroupListExprDef());
        accessTree.addExpressionCombination(new AuthUserGroupExprDef(), new NotInExprDef(), new GroupListExprDef());

        accessTree.addExpressionCombination(new NamedAttributeValueExprDef(), new EqualsExprDef(),
            new StringValueExprDef());


        return accessTree;
    }

    private static ExprDef buildValidationTree()
    {
        ExprDef validationTree = new RootExprDef();

        validationTree.addExpressionCombination(
            new AttributeValueExprDef(),
            new IPv4ExprDef());

        validationTree.addExpressionCombination(
            new AttributeValueExprDef(),
            new EmailExprDef());

        validationTree.addExpressionCombination(
            new AttributeValueExprDef(),
            new IPv6ExprDef());

        validationTree.addExpressionCombination(
            new AttributeValueExprDef(),
            new RegexMatchesExprDef(),
            new RegexValueExprDef());

        validationTree.addExpressionCombination(
            new AttributeValueExprDef(),
            new NumberExprDef(),
            new StringValueExprDef());

        validationTree.addExpressionCombination(
            new AttributeValueExprDef(),
            new NotRegexMatchesExprDef(),
            new RegexValueExprDef());

        validationTree.addExpressionCombination(
            new AttributeValueExprDef(),
            new EqualsExprDef(),
            new StringValueExprDef());

        validationTree.addExpressionCombination(
            new AttributeValueExprDef(),
            new NotEqualsExprDef(),
            new StringValueExprDef());

        validationTree.addExpressionCombination(
            new AttributeValueExprDef(),
            new MandatoryExprDef()
        );

        return validationTree;
    }

    public Expr loadExpr(UUID<Expr> expressionId)
    {
        List<Expr> result = em.createQuery("select e from Expr e where e.exprId = :eId")
            .setParameter("eId", expressionId)
            .getResultList();

        Expr root = null;

        //find the root
        for (Expr expr : result)
        {
            if (expr.isRoot())
            {
                root = expr;
                break;
            }
        }
        return root;
    }

    /**
     * Given a RuleType + the leaf parts of an expression return the next available operand.
     * If there are no available operands then return an empty list.
     */

    public List<ExprDef> getNextLegalExprDefsForPartialExpression(RuleType ruleType, ExprDef... exprDefs)
    {
        ExprDef root = expressionRules.get(ruleType);

        if (root == null)
            return new ArrayList<ExprDef>();

        return root.getNextLegalExprDefs(exprDefs);
    }


    public Map<String, Object> createExpressionContext()
    {
        HashMap<String, Object> expressionContext = new HashMap<String, Object>();
        expressionContext.put(AuthUserExprDef.AUTHENTICATED_USER_KEY, securityService.getActiveIndividual().value());

        return expressionContext;
    }

    public ExprEvalStrategyFactory getExprEvalStrategyFactory()
    {
        return exprEvalStrategyFactory;
    }


    public void mergeExpr(Expr expression)
    {
        em.merge(expression);
    }

    public void recursiveReplaceExprId(Expr expression, UUID rootId)
    {
        expression.setExprId(rootId);

        List<Expr> children = expression.getChildren();
        if (children != null)
        {
            for (Expr expr : children)
            {
                recursiveReplaceExprId(expr, rootId);
            }
        }
    }
}
