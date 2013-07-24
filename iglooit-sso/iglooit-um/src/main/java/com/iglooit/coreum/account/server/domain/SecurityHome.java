package com.iglooit.coreum.account.server.domain;

import com.iglooit.commons.iface.type.AppX;
import com.iglooit.coreum.security.iface.access.domain.Privilege;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.SecondLevelCacheStatistics;
import org.hibernate.stat.Statistics;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.validation.constraints.NotNull;

@Component("securityHome")
public class SecurityHome
{
    @NotNull
    @PersistenceContext(unitName = "oss")
    private EntityManager em;

    private static final Log LOG = LogFactory.getLog(SecurityHome.class);

    private void printStats()
    {
        Object o = em.getDelegate();
        if (o instanceof org.hibernate.Session)
        {
            org.hibernate.Session s = (org.hibernate.Session)o;

            Statistics stats = s.getSessionFactory().getStatistics();

            SecondLevelCacheStatistics secondLevelCacheStatistics = stats.getSecondLevelCacheStatistics(
                Privilege.class.getName());
            EntityStatistics privilegeStats = stats.getEntityStatistics("Privilege");

            LOG.trace("Privilege fetches: " + privilegeStats.getFetchCount());
            LOG.trace("Privilege loads: " + privilegeStats.getLoadCount());

            LOG.trace("SecondLevelCache hits: " + secondLevelCacheStatistics.getHitCount());
            LOG.trace("SecondLevelCache misses: " + secondLevelCacheStatistics.getMissCount());
            LOG.trace("SecondLevelCache puts: " + secondLevelCacheStatistics.getPutCount());
            LOG.trace("SecondLevelCache memory count: " + secondLevelCacheStatistics.getElementCountInMemory());

            LOG.trace("QueryCache hits: " + stats.getQueryCacheHitCount());
            LOG.trace("QueryCache misses: " + stats.getQueryCacheMissCount());
            LOG.trace("QueryCache misses: " + stats.getQueryCachePutCount());
        }
    }

    public Privilege findPrivilegeByName(final String privilegeName)
    {
        Query query = em.createNamedQuery("privilege.forname").setParameter("privName", privilegeName);
        Privilege result;
        try
        {
            result = (Privilege)query.getSingleResult();
        }
        catch (NoResultException nre)
        {
            throw new AppX("unable to find privilege named '" + privilegeName + "'");
        }

        if (LOG.isTraceEnabled())
        {
            printStats();
        }

        return result;
    }
}
