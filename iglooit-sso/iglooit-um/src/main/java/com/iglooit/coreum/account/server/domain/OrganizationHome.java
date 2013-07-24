package com.iglooit.coreum.account.server.domain;

import com.iglooit.commons.iface.type.AppX;
import com.iglooit.coreum.account.iface.domain.Organization;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.validation.constraints.NotNull;

@Component("organizationHome")
public class OrganizationHome
{
    @NotNull
    @PersistenceContext(unitName = "oss")
    private EntityManager em;

    public OrganizationHome()
    {
    }

    public Organization findByName(final String name)
    {
        try
        {
            return (Organization)em.createQuery(
                "select a from Organization a where a.name = :name")
                .setParameter("name", name)
                .getSingleResult();
        }
        catch (NoResultException nre)
        {
            throw new AppX("Could not find account for name: " + name);
        }
    }
}
