package com.iglooit.core.security.iface.access.domain;

import com.iglooit.commons.iface.domain.Validator;
import com.iglooit.commons.iface.domain.meta.Meta;
import com.iglooit.core.base.iface.domain.NamedJpaDomainEntity;
import com.iglooit.core.expr.iface.domain.ExprEvalStrategyFactory;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * represents the right to perform an action, or access some data
 */
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY)

@NamedQueries({
    @NamedQuery(
        name = "privilege.forname",
        query = "select p from Privilege p where p.name = :privName",
        hints = {
            @QueryHint(name = "openjpa.hint.OptimizeResultCount", value = "1"),
            @QueryHint(name = "org.hibernate.readOnly", value = "true"),
            @QueryHint(name = "org.hibernate.cacheable", value = "true")
        }
    )
})
@Entity
@Table(name = "PRIVES")
public class Privilege extends NamedJpaDomainEntity<Privilege>
{
    @OneToMany(mappedBy = "privilege", cascade = CascadeType.ALL)
    @Cascade({org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private List<PrivilegeRule> privilegeRules;

    protected Meta createMetaDelegate()
    {
        return new PrivilegeMeta(this);
    }

    public Privilege()
    {

    }

    public List<Validator> doGetValidators()
    {
        return new ArrayList<Validator>();
    }

    @Deprecated
    public Privilege(String name, String description)
    {
        super(name, description);
    }

    public Privilege(PrivilegeConst privilege, String description)
    {
        super(privilege.getPrivilegeString(), description);
    }

    public List<PrivilegeRule> getPrivilegeRules()
    {
        return privilegeRules;
    }

    public void setPrivilegeRules(List<PrivilegeRule> privilegeRules)
    {
        this.privilegeRules = privilegeRules;
    }

    public boolean isGranted(ExprEvalStrategyFactory evalStrategyFactory, Subject subject, RestrictedResource resource)
    {
        //assuming at this point that the Subject has the Privilege, and that the relationhip
        //between the RestrictedResource and the SecurityZone is appropriate (ie it has been
        // taken care of in the RestrictedResource logic)

        //there may be no rules - so only fail when we do have a rule and it comes back negative
        for (PrivilegeRule privilegeRule : privilegeRules)
        {
            if (!privilegeRule.execute(evalStrategyFactory, subject, resource))
                return false;
        }
        return true;
    }

    @Override
    public String toString()
    {
        return "[Privilege: [name: " + getName() +
            " description: " + getDescription() + "]]";
    }
}
