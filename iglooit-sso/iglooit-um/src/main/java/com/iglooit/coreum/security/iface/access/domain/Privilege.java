package com.iglooit.coreum.security.iface.access.domain;

import com.iglooit.coreum.base.iface.domain.NamedJpaDomainEntity;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;

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
    public Privilege()
    {
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

    @Override
    public String toString()
    {
        return "[Privilege: [name: " + getName() +
            " description: " + getDescription() + "]]";
    }
}
