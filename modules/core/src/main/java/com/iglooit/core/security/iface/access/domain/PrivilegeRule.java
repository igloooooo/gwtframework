package com.iglooit.core.security.iface.access.domain;

import com.clarity.commons.iface.domain.Validator;
import com.clarity.commons.iface.domain.meta.Meta;
import com.clarity.commons.iface.type.UUID;
import com.clarity.core.base.iface.domain.NamedJpaDomainEntity;
import com.clarity.core.expr.iface.domain.Expr;
import com.clarity.core.expr.iface.domain.ExprEvalStrategyFactory;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "PRIVILEGE_RULES")
public class PrivilegeRule extends NamedJpaDomainEntity<PrivilegeRule>
{
    @ManyToOne
    @JoinColumn(name = "PRIR_RULE_ID", nullable = false)
    private Expr rule;

    @ManyToOne
    @JoinColumn(name = "PRIR_PRIV_ID", nullable = false)
    private Privilege privilege;

    protected Meta createMetaDelegate()
    {
        return new PrivilegeRuleMeta(this);
    }

    public PrivilegeRule()
    {
    }

    protected Expr getRule()
    {
        return rule;
    }

    protected void setRule(Expr rule)
    {
        this.rule = rule;
    }

    protected Privilege getPrivilege()
    {
        return privilege;
    }

    protected void setPrivilege(Privilege privilege)
    {
        this.privilege = privilege;
    }

    public List<Validator> doGetValidators()
    {
        return new ArrayList<Validator>();
    }

    public PrivilegeRule(String name, String description, Expr rule, Privilege privilege)
    {
        super(name, description);
        this.rule = rule;
        this.privilege = privilege;
    }

    public PrivilegeRule(Expr rule, Privilege privilege)
    {
        this.rule = rule;
        this.privilege = privilege;
    }

    public PrivilegeRule(UUID id, String name, String description, Expr rule, Privilege privilege)
    {
        super(id, name, description);
        this.rule = rule;
        this.privilege = privilege;
    }

    //note - this rule should not be called on the client 
    public boolean execute(ExprEvalStrategyFactory evalStrategyFactory, Subject subject, RestrictedResource resource)
    {

        Map<String, Object> expressionContext = new HashMap<String, Object>();

        return (Boolean)rule.eval(evalStrategyFactory, expressionContext).getResult();
    }
}
