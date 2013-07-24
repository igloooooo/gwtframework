package com.iglooit.coreum.oss.server;

import com.iglooit.commons.iface.type.Option;
import com.iglooit.commons.iface.um.UserPasswordUpdateStatus;
import com.iglooit.commons.server.security.EncryptionProcessor;
import com.iglooit.coreum.account.iface.domain.UserRole;
import com.iglooit.coreum.base.server.domain.ServerUUIDFactory;
import com.iglooit.coreum.base.server.util.OracleUserManagementUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.util.List;

@Component("ossHome")
public class OSSHome
{
    private static final Log LOG = LogFactory.getLog(OSSHome.class);

    private static OSSHome instance;

    @NotNull
    @PersistenceContext(unitName = "oss")
    private EntityManager ossEm;

    @Resource(name = "umTransactionManager")
    private JpaTransactionManager txManager;

    private JdbcTemplate jdbc;

    @Resource
    private ServerUUIDFactory serverUUIDFactory;

    public OSSHome()
    {
        if (instance == null)
            instance = this;
    }

    @Resource
    protected void setOssDataSource(@Qualifier("umDataSource") DataSource ds)
    {
        jdbc = new JdbcTemplate(ds);
    }

    public UserPasswordUpdateStatus changeUserPassword(final String username, final String password,
                                                       final String newPassword)
    {
        try
        {
            OracleUserManagementUtil.ossChangePassword(jdbc, username.toUpperCase(), newPassword, password);
            if (LOG.isDebugEnabled()) LOG.debug("Oracle password changed without exception.");
            changeBssPassword(username, newPassword);
            if (LOG.isDebugEnabled()) LOG.debug("BSS password synced without exception.");
        }
        catch (Exception e)
        {
            LOG.error("Could not set oracle newPassword for user: " + username + ", " + e.getMessage());
            return UserPasswordUpdateStatus.getByException(e.getMessage());
        }
        return UserPasswordUpdateStatus.PASSWORD_VALID;
    }

    public UserPasswordUpdateStatus setUserPassword(final String username, final String newPassword)
    {
        try
        {
            OracleUserManagementUtil.ossSetPassword(jdbc, username.toUpperCase(), newPassword);
            if (LOG.isDebugEnabled()) LOG.debug("Oracle password set without exception.");
            changeBssPassword(username, newPassword);
            if (LOG.isDebugEnabled()) LOG.debug("BSS password synced without exception.");
        }
        catch (Exception e)
        {
            LOG.error("Could not set oracle newPassword for user: " + username + ", " + e.getMessage());
            return UserPasswordUpdateStatus.getByException(e.getMessage());
        }
        return UserPasswordUpdateStatus.PASSWORD_VALID;
    }

    public Option<UserRole> getPartyUserName(final String username)
    {
        List<UserRole> matchingList = ossEm.createQuery(
            "select p from UserRole p where lower( p.username ) = :userName")
            .setParameter("userName", username.toLowerCase()).getResultList();
        if (matchingList.size() != 1 || matchingList.get(0) == null)
            return Option.none();
        return Option.some(matchingList.get(0));
    }

    public boolean syncBssPassword(final String username, final String password)
    {
        try
        {
            changeBssPassword(username, password);
            return true;
        }
        catch (Exception e)
        {
            LOG.error("Could not sync password for user: " + username + ", " + e.getMessage());
            return false;
        }
    }

    private void changeBssPassword(final String username, final String password) throws Exception
    {
        Option<UserRole> userRoleOpt = getPartyUserName(username);
        UserRole userRole = userRoleOpt.value();
        userRole.setPassword(password);
        EncryptionProcessor.encrypt(userRole, true);
    }
}
