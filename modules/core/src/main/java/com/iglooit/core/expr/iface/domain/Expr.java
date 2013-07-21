package com.iglooit.core.expr.iface.domain;

import com.iglooit.commons.iface.domain.Validator;
import com.iglooit.commons.iface.type.AppX;
import com.iglooit.commons.iface.type.UUID;
import com.iglooit.core.base.iface.domain.JpaDomainEntity;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "EXPRESSIONS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(
    name = "EXPR_EXPRTYPE",
    discriminatorType = DiscriminatorType.STRING)
public abstract class Expr<InheritingClass extends Expr> extends JpaDomainEntity<InheritingClass>
{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EXPR_PARENT_ID", nullable = true)
    private Expr parent;

    @Column(name = "EXPR_CHILDORDER", nullable = false)
    private int childOrder;

    @Type(type = "com.clarity.core.lib.server.hibernate.UUIDUserType")
    @Column(name = "EXPR_EXPRID", columnDefinition = "raw(16)", nullable = true)
    private UUID<Expr> exprId;

    @Column(name = "EXPR_STRINGVALUE", nullable = true)
    private String stringValue;

    //todo at: move ordering to new order style (confusion....)
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    @OrderBy("childOrder asc")
    private List<Expr> children;

    @Column(name = "EXPR_EXPRTYPE", insertable = false, updatable = false)
    private String type;

    public Expr()
    {
    }

    public Expr(UUID<Expr> exprId)
    {
        this.exprId = exprId;
    }

    public Expr(UUID<Expr> exprId, String stringValue)
    {
        this.exprId = exprId;
        this.stringValue = stringValue;
    }

    private static final Comparator<? super Expr> COMPARATOR = new Comparator<Expr>()
    {
        //@Override -seems to be breaking the jasper compile?
        public int compare(Expr lhs, Expr rhs)
        {
            if (lhs.childOrder == rhs.childOrder)
                return 0;
            else if (lhs.childOrder < rhs.childOrder)
                return -1;
            else
                return 1;
        }
    };

    //    This method was implemented as part of the save a very small amount of time now
//    and pay a lot more later scheme
//    and should be replaced ASAP by a @OneToMany to ExprDef once it is made to be an JPADomainEntity.
    public abstract ExprDef getExprDef();

    public abstract ExprResult eval(ExprEvalStrategyFactory exprEvalStrategyFactory,
                                    Map<String, Object> expressionContext);

    public int getChildOrder()
    {
        return childOrder;
    }

    public void setChildOrder(int childOrder)
    {
        this.childOrder = childOrder;
    }

    public UUID getExprId()
    {
        return exprId;
    }

    public void setExprId(UUID exprId)
    {
        this.exprId = exprId;
    }

    public Expr getParent()
    {
        return parent;
    }

    public void setParent(Expr parent)
    {
        this.parent = parent;
    }

    public List<Expr> getChildren()
    {
        return children;
    }

    public void setChildren(List<Expr> children)
    {
        this.children = children;
    }

    public final void ensureCorrectNumberofChildren(int childCount, Class... clazzes)
    {
        //todo jm-at: reflection not GWT compatible!!!
//        final String exprName = getClass().getSimpleName();
        final String exprName = "test";

        if (childCount == 0)
        {
            if (!(getChildren() == null || getChildren().size() == 0))
            {
                throw new AppX(exprName + " must have no children");
            }
        }
        else
        {
            if (getChildren() == null || getChildren().size() != childCount)
                throw new AppX(exprName + " must have exactly " + childCount + " children. Currently " +
                    "has " + getChildren().size());

            if (clazzes.length != 0 && clazzes.length != childCount)
                throw new AppX("Return type classes list must be either 0 length or contain one type per child");


            int i = 0;
            for (Expr expr : getChildren())
            {
                if (expr == null)
                    throw new AppX(exprName + " has null children.");

                //todo mg: fix this - at the moment it assumes that the child will always have the same
                //expression type as the parent.
//                if (clazzes.length == childCount)
//                {
//                    if (!clazzes[i].equals(expr.getReturnType()))
//                        throw new AppX("Invalid or unexpected return type for expression: " +
//                            expr.getReturnType());
//                }

                i++;
            }
        }
    }

    public abstract boolean canHaveChildren();

    public final boolean isOperator()
    {
        return this.canHaveChildren();
    }

    public final boolean isLeaf()
    {
        return !this.canHaveChildren();
    }

    public abstract Class getReturnType();

    private void setupAndAddChild(Expr expr)
    {
        expr.setParent(this);
        expr.childOrder = getChildren().size();

        getChildren().add(expr);

    }

    /**
     * Important to note that the order passed in here is the order that will be preserved in tree.
     *
     * @param args the sub expressions to add to the current expression
     */
    public void setupSubExpressions(Expr... args)
    {
        if (!canHaveChildren())
            throw new AppX("This expression cannot contain children: " + this);

        if (children == null)
            children = new ArrayList<Expr>();
        if (children.size() != 0)
            throw new AppX("setup Arguments should only be called once");

        for (Expr child : args)
        {
            setupAndAddChild(child);
        }
    }

    public void addChild(Expr child)
    {
        if (!canHaveChildren())
            throw new AppX("This expression cannot contain children: " + this);

        if (children == null)
        {
            children = new ArrayList<Expr>();
        }

        setupAndAddChild(child);
    }

    public void setStringValue(String stringValue)
    {
        this.stringValue = stringValue;
    }

    public String getStringValue()
    {
        return stringValue;
    }


    @Override
    public List<Validator> doGetValidators()
    {
        return Collections.emptyList();
    }

    public boolean isRoot()
    {
        return getParent() == null;
    }

    public ExprResult evalSubExpr(int position, Map<String, Object> expressionContext,
                                  ExprEvalStrategyFactory exprEvalStrategyFactory)
    {
        if (children == null)
            throw new AppX("children is null - there are no subexpressions");

        if (position >= children.size() || children.get(position) == null)
            throw new AppX("there is no subexpression in position " + position + ".");

        return children.get(position).eval(exprEvalStrategyFactory, expressionContext);
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }
}
