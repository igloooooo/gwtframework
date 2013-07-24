package com.iglooit.sso.cas;

import com.iglooit.commons.iface.type.Tuple3;
import com.iglooit.um.server.UserSecurityService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.authentication.AbstractPasswordPolicyEnforcer;
import org.jasig.cas.authentication.LdapPasswordPolicyEnforcementException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

/**
 * The password policy enforcer that queries Oracle for password expiry.
 */
public class ClarityPasswordPolicyEnforcer extends AbstractPasswordPolicyEnforcer
{
    private static final Log LOG = LogFactory.getLog(ClarityPasswordPolicyEnforcer.class);

    private static final int PASSWORD_STATUS_PASS = -1;
    private static final int PASSWORD_STATUS_EXPIRES_SOON = -2;
    private static final int WARN_DAYS_DEFAULT = 14;

    @PersistenceContext(unitName = "oss")
    private EntityManager ossEm;

    @Resource
    private UserSecurityService securityService;

    private JdbcTemplate jdbcTemplate;

    private int warnDays = WARN_DAYS_DEFAULT;

    @Resource
    protected void setOssDataSource(@Qualifier("umDataSource") DataSource ds)
    {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    public void setWarnDays(int warnDays)
    {
        this.warnDays = warnDays;
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
    }

    /**
     * @return Number of days left to the expiration date, or {@value #PASSWORD_STATUS_PASS}
     */
    public long getNumberOfDaysToPasswordExpirationDate(final String userId)
        throws LdapPasswordPolicyEnforcementException
    {
        String username = userId;
        String[] organizationIndividual = username.split("/");
        if (organizationIndividual.length >= 2)
            username = organizationIndividual[1];
        return getExpiryDays(username);
    }

    public int getExpiryDays(final String userId) throws
        LdapPasswordPolicyEnforcementException
    {
        List<Tuple3<String, Date, Date>> userRecords;
        final String sql = "select account_status, expiry_date, sysdate from dba_users" +
            " where account_status like '%GRACE%' and username = ?";
        PreparedStatementCreator psCreator = new UserExpiryPreparedStatementCreator(sql, userId);
        userRecords = jdbcTemplate.query(psCreator, new RowMapper<Tuple3<String, Date, Date>>()
        {
            @Override
            public Tuple3<String, Date, Date> mapRow(ResultSet rs, int rowNum) throws SQLException
            {
                Tuple3<String, Date, Date> resultTuple = new Tuple3<String, Date, Date>();
                resultTuple.setFirst(rs.getString(1));
                resultTuple.setSecond(rs.getDate(2));
                resultTuple.setThird(rs.getDate(3));
                return resultTuple;
            }
        });
        if (userRecords == null || userRecords.size() != 1)
            return PASSWORD_STATUS_PASS;
        Tuple3<String, Date, Date> userRecord = userRecords.get(0);
        String accountStatus = userRecord.getFirst();
        Date expiryDate = userRecord.getSecond();
        Date currentDate = userRecord.getThird();
        if (expiryDate == null)
            return PASSWORD_STATUS_EXPIRES_SOON;
        LOG.info("User " + userId + "'s status is: " + accountStatus + " expires: " + expiryDate);
        long timeDiff = expiryDate.getTime() - currentDate.getTime();
        if (timeDiff < 0)
        {
            String msgToLog = "Authentication failed because account password has already expired.";
            final LdapPasswordPolicyEnforcementException exc = new LdapPasswordPolicyEnforcementException(msgToLog);
            LOG.warn(msgToLog, exc);
            throw exc;
        }
        long days = timeDiff / (24 * 3600 * 1000);
        if (days > warnDays)
            return PASSWORD_STATUS_PASS;
        return (int)days;
    }
}
