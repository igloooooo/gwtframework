package com.iglooit.core.lib.server.security.impl;

import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.type.Option;
import com.clarity.commons.iface.type.SecurityX;
import com.clarity.commons.iface.type.UUID;
import com.clarity.core.account.iface.domain.PartyRole;
import com.clarity.core.account.iface.domain.UserRole;
import com.clarity.core.iface.SecurityService;
import com.clarity.core.security.iface.access.domain.Privilege;
import com.clarity.core.security.iface.access.domain.PrivilegeConst;
import com.clarity.core.security.iface.access.domain.Subject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.stat.EntityStatistics;
import org.hibernate.stat.SecondLevelCacheStatistics;
import org.hibernate.stat.Statistics;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component("securityHome")
public class SecurityHome
{
    @Resource
    private SecurityService securityService;

    @PersistenceContext(unitName = "oss")
    private EntityManager em;

    private static final Log LOG = LogFactory.getLog(SecurityHome.class);

    //jm: the below comment refers to using hashmaps at all for caching.
    //todo mg: not convinced this is a good idea - probably will get away with it becuase Privileges shouldn't
    //be changed - but still.... Have a think on this - trying not to subvert the lazy loading on the security
    //checking methods

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

    public Statistics getCacheStats()
    {
        return ((org.hibernate.Session)em.getDelegate()).getSessionFactory().getStatistics();
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

    public Subject getSubjectById(UUID subjectId)
    {
        UserRole ur = (UserRole)em.createQuery("select ur from UserRole ur " +
            "left join fetch ur.userOrgSecurityRoles uosr " +
            "left join fetch uosr.orgSecurityRole osr " +
            "left join fetch osr.securityRole sr " +
            "left join fetch sr.rolePrivileges where ur.id = ?1")
            .setParameter(1, subjectId)
            .setHint("org.hibernate.cacheable", "false")
            .getSingleResult();
        return ur;
    }

    public List<Privilege> getPrivileges()
    {
        final UUID<PartyRole> userRoleId = securityService.getActiveUserRoleId().value();
        List<Privilege> privileges = em.createQuery(
                "select distinct p.privilege from UserRole ur " +
                    "left join ur.userOrgSecurityRoles uosr " +
                    "left join uosr.orgSecurityRole osr " +
                    "left join osr.securityRole sr " +
                    "left join sr.rolePrivileges p " +
                    "where ur.id = ?1")
            .setParameter(1, userRoleId)
            .setHint("org.hibernate.cacheable", "false")
            .getResultList();
        return privileges;
    }

    public void checkPrivileges(PrivilegeConst[] requiredPrivileges)
    {
        checkPrivileges(null, requiredPrivileges);
    }

    public void checkPrivileges(HttpServletRequest request, PrivilegeConst[] requiredPrivileges)
    {
        // this may throw a SessionExpiredX
        //review ms(mg) : handle the none case (possibly use orDefault()??)
        boolean permissionGranted = false;
        Option<UUID>  activeUserRoleId = securityService.getActiveUserRoleId(request);
        if (activeUserRoleId.isNone())
                throw new UnsupportedOperationException("This operation is not supported for current user");

        Subject activeSubject = getSubjectById(activeUserRoleId.value());

        if (requiredPrivileges.length == 0)
            throw new AppX("Request handler does not specify any required privileges");

        StringBuilder requiredPrivilegesString = new StringBuilder();
        for (PrivilegeConst privilegeConst : requiredPrivileges)
        {
            if (requiredPrivilegesString.length() > 0)
                requiredPrivilegesString.append(", ");
            requiredPrivilegesString.append(privilegeConst);

            Privilege requiredPrivilege = getPrivilegeForPrivilegeConst(privilegeConst);
            if (activeSubject.hasPrivilege(requiredPrivilege))
            {
                permissionGranted = true;
                break;
            }
        }
        if (!permissionGranted)
            throw new SecurityX("Insufficient privileges for current party: subject = " +
                activeSubject.toString() + ", required privilege = " + requiredPrivilegesString.toString());
    }

    private Privilege getPrivilegeForPrivilegeConst(PrivilegeConst priv)
    {
        return findPrivilegeByName(priv.getPrivilegeString());
    }
}
