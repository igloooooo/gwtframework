package com.iglooit.coreum.account.server.domain;

import com.iglooit.commons.iface.type.AppX;
import com.iglooit.commons.iface.type.UUID;
import com.iglooit.coreum.account.iface.domain.Individual;
import com.iglooit.coreum.base.server.domain.ServerUUIDFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;

@Component("individualHome")
public class IndividualHome
{
    @NotNull
    @PersistenceContext(unitName = "oss")
    private EntityManager em;

    @Resource
    private OrganizationHome organizationHome;

    @Resource
    private ServerUUIDFactory serverUUIDFactory;

    @Resource
    protected void setDataSource(@Qualifier("umDataSource") DataSource ds)
    {
    }

    public IndividualHome()
    {
    }

    public Individual getParty(UUID partyId)
    {
        // Individual result = em.getReference(Individual.class, partyId);
        Individual result = (Individual)em.createQuery("from Individual indi " +
            "join fetch indi.organization " +
            "where indi.id = ?1").setParameter(1, partyId).getSingleResult();
        if (result == null)
        {
            throw new AppX("Party does not exist for " + partyId.toString());
        }
        return result;
    }
}
