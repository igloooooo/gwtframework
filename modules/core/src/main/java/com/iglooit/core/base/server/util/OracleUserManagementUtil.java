package com.iglooit.core.base.server.util;

import com.clarity.commons.iface.type.AppX;
import com.clarity.commons.iface.type.Tuple2;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.datasource.AbstractDataSource;

import javax.persistence.EntityManager;
import javax.sql.DataSource;
import java.nio.charset.Charset;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class OracleUserManagementUtil
{
    private static final Log LOG = LogFactory.getLog(OracleUserManagementUtil.class);
    public static final String USER_MANAGER_ROLE = "CO_USER_MANAGER";
    private static final String USER_MANAGER_ROLE_PASSWORD = new String(new byte[]{99, 108, 116, 121, 33, 50, 51, 52},
        Charset.forName("ISO-8859-1"));
    private static final String ORACLE_ERROR_ROLE_NOT_GRANTED = "ORA-01951";

    public static JdbcTemplate prepareJdbcTemplate(EntityManager entityManager)
    {
        DataSource db = new ConnectionCachingDataSource(entityManager);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(db);

        StringBuilder rolesStringBuilder = new StringBuilder(1024);
        boolean roleFound = false;
        int numberOfRoles = 0;

        List<String> roles = jdbcTemplate.queryForList("select role from session_roles order by role", String.class);
        for (String role : roles)
        {
            if (USER_MANAGER_ROLE.equals(role))
            {
                LOG.debug("Required Role Found!");
                roleFound = true;
                break;
            }
            if (rolesStringBuilder.length() > 0)
                rolesStringBuilder.append(", ");
            rolesStringBuilder.append(role);
            numberOfRoles++;
        }

        if (!roleFound)
        {
            LOG.debug("Existing roles (" + numberOfRoles + "): " + rolesStringBuilder.toString());
            String setRolesql =
                "set role " + USER_MANAGER_ROLE + " identified by \"" + USER_MANAGER_ROLE_PASSWORD + "\"";
            if (rolesStringBuilder.length() > 0)
                setRolesql += ", " + rolesStringBuilder.toString();
            try
            {
                jdbcTemplate.execute(setRolesql);
            }
            catch (BadSqlGrammarException e)
            {
                // We don't wrap the original exception as it includes sensitive information.
                throw new AppX("Unable to activate user manager role, due to an exception.");
            }
        }

        return jdbcTemplate;
    }

    public static void ossUserCreate(EntityManager entityManager, final String ossUserId, final String ossProfile)
    {
        JdbcTemplate jdbcTemplate = prepareJdbcTemplate(entityManager);
        List<SqlParameter> params = new ArrayList<SqlParameter>();
        params.add(new SqlOutParameter("result", Types.INTEGER));
        params.add(new SqlParameter("p_empe_userid", Types.VARCHAR));
        params.add(new SqlParameter("p_empe_profile", Types.VARCHAR));
        params.add(new SqlOutParameter("p_ret_message", Types.VARCHAR));
        Map<String, Object> result = jdbcTemplate.call(new CallableStatementCreator()
        {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException
            {
                String callProcSQL = "{? = call clarity_admin.p_employee.jdbc_create_user(?, ?, ?)}";
                CallableStatement proc = con.prepareCall(callProcSQL);
                proc.registerOutParameter(1, Types.INTEGER);
                proc.setString(2, ossUserId);
                proc.setString(3, ossProfile);
                proc.registerOutParameter(4, Types.VARCHAR);
                return proc;
            }
        }, params);
        LOG.debug("jdbc_create_user result: " + result);
        if ((Integer)result.get("result") != 1)
            throw new AppX("Unable to create the user: " + result.get("p_ret_message"));
    }

    public static void ossResetPassword(EntityManager entityManager, final String ossUserId, final String password)
    {
        JdbcTemplate jdbcTemplate = prepareJdbcTemplate(entityManager);
        List<SqlParameter> params = new ArrayList<SqlParameter>();
        params.add(new SqlOutParameter("result", Types.INTEGER));
        params.add(new SqlParameter("p_empe_userid", Types.VARCHAR));
        params.add(new SqlParameter("p_password", Types.VARCHAR));
        params.add(new SqlOutParameter("p_ret_message", Types.VARCHAR));
        Map<String, Object> result = jdbcTemplate.call(new CallableStatementCreator()
        {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException
            {
                String callProcSQL = "{? = call sys.p_clty_user_admin.jdbc_reset_password(?, ?, ?)}";
                CallableStatement proc = con.prepareCall(callProcSQL);
                proc.registerOutParameter(1, Types.INTEGER);
                proc.setString(2, ossUserId);
                proc.setString(3, password);
                proc.registerOutParameter(4, Types.VARCHAR);
                return proc;
            }
        }, params);
        LOG.debug("jdbc_reset_password result: " + result);
        if ((Integer)result.get("result") != 1)
            throw new AppX("Unable to reset user password: " + result.get("p_ret_message"));
    }

    public static void ossResetPasswordDontExpire(EntityManager entityManager, final String ossUserId,
                                                  final String password, final String existingPassword)
    {
        JdbcTemplate jdbcTemplate = prepareJdbcTemplate(entityManager);
        List<SqlParameter> params = new ArrayList<SqlParameter>();
        params.add(new SqlOutParameter("result", Types.INTEGER));
        params.add(new SqlParameter("p_empe_userid", Types.VARCHAR));
        params.add(new SqlParameter("p_password", Types.VARCHAR));
        params.add(new SqlParameter("p_existing_password", Types.VARCHAR));
        params.add(new SqlOutParameter("p_ret_message", Types.VARCHAR));
        Map<String, Object> result = jdbcTemplate.call(new CallableStatementCreator()
        {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException
            {
                String callProcSQL = "{? = call sys.p_clty_user_admin.jdbc_change_password(?, ?, ?, ?)}";
                CallableStatement proc = con.prepareCall(callProcSQL);
                proc.registerOutParameter(1, Types.INTEGER);
                proc.setString(2, ossUserId);
                proc.setString(3, password);
                proc.setString(4, existingPassword);
                proc.registerOutParameter(5, Types.VARCHAR);
                return proc;
            }
        }, params);
        LOG.debug("jdbc_change_password result: " + result);
        if ((Integer)result.get("result") != 1)
            throw new AppX("Unable to reset user password: " + result.get("p_ret_message"));
    }

    public static void ossChangeUserProfile(EntityManager entityManager, final String ossUserId,
                                            final String ossProfile)
    {
        JdbcTemplate jdbcTemplate = prepareJdbcTemplate(entityManager);
        List<SqlParameter> params = new ArrayList<SqlParameter>();
        params.add(new SqlOutParameter("result", Types.INTEGER));
        params.add(new SqlParameter("p_empe_userid", Types.VARCHAR));
        params.add(new SqlParameter("p_empe_profile", Types.VARCHAR));
        params.add(new SqlOutParameter("p_ret_message", Types.VARCHAR));
        Map<String, Object> result = jdbcTemplate.call(new CallableStatementCreator()
        {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException
            {
                String callProcSQL = "{? = call sys.p_clty_user_admin.jdbc_change_user_profile(?, ?, ?)}";
                CallableStatement proc = con.prepareCall(callProcSQL);
                proc.registerOutParameter(1, Types.INTEGER);
                proc.setString(2, ossUserId);
                proc.setString(3, ossProfile);
                proc.registerOutParameter(4, Types.VARCHAR);
                return proc;
            }
        }, params);
        LOG.debug("jdbc_change_user_profile result: " + result);
        if ((Integer)result.get("result") != 1)
            throw new AppX("Unable to change user profile: " + result.get("p_ret_message"));
    }

    public static void grantOssRole(EntityManager entityManager, final String userId, final String roleName)
    {
        Tuple2<Integer, String> result = grantOssRole(entityManager, userId, roleName, "G");
        boolean success = (result.getFirst() != 0);
        String resultString = result.getSecond();
        if (!success)
            throw new AppX("unable to grant role '" + roleName + "' reason = " + resultString);
    }

    public static void revokeOssRole(EntityManager entityManager, String userId, String roleName)
    {
        Tuple2<Integer, String> result = grantOssRole(entityManager, userId, roleName, "R");
        boolean success = (result.getFirst() != 0);
        String resultString = result.getSecond();
        if (!success && !resultString.contains(ORACLE_ERROR_ROLE_NOT_GRANTED))
            throw new AppX("unable to revoke role '" + roleName + "' reason = " + resultString);
    }

    public static Tuple2<Integer, String> grantOssRole(EntityManager entityManager, final String userId,
                                                       final String roleName, final String grantOrRevoke)
    {
        JdbcTemplate jdbcTemplate = prepareJdbcTemplate(entityManager);
        List<SqlParameter> params = new ArrayList<SqlParameter>();
        params.add(new SqlOutParameter("result", Types.INTEGER));
        params.add(new SqlParameter("p_empe_user_id", Types.VARCHAR));
        params.add(new SqlParameter("p_empe_role", Types.VARCHAR));
        params.add(new SqlParameter("p_empe_grant", Types.VARCHAR));
        params.add(new SqlOutParameter("p_ret_message", Types.VARCHAR));
        Map<String, Object> result = jdbcTemplate.call(new CallableStatementCreator()
        {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException
            {
                String callProcSQL = "{? = call clarity_admin.p_employee.jdbc_grant_access(?, ?, ?, ?)}";
                CallableStatement proc = con.prepareCall(callProcSQL);
                proc.registerOutParameter(1, Types.INTEGER);
                proc.setString(2, userId);
                proc.setString(3, roleName);
                proc.setString(4, grantOrRevoke);
                proc.registerOutParameter(5, Types.VARCHAR);
                return proc;
            }
        }, params);
        LOG.debug("jdbc_grant_access result: " + result);
        Integer resultCode = (Integer)result.get("result");
        String resultString = (String)result.get("p_ret_message");
        return new Tuple2<Integer, String>(resultCode, resultString);
    }

    public static void enableUser(EntityManager entityManager, final String userId)
    {
        JdbcTemplate jdbcTemplate = prepareJdbcTemplate(entityManager);
        List<SqlParameter> params = new ArrayList<SqlParameter>();
        params.add(new SqlOutParameter("result", Types.INTEGER));
        params.add(new SqlParameter("EMPE_USERID", Types.VARCHAR));
        params.add(new SqlOutParameter("v_ret_message", Types.VARCHAR));
        Map<String, Object> result = jdbcTemplate.call(new CallableStatementCreator()
        {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException
            {
                String callProcSQL = "{? = call clarity_admin.p_employee.jdbc_enable_user(?, ?)}";
                CallableStatement proc = con.prepareCall(callProcSQL);
                proc.registerOutParameter(1, Types.INTEGER);
                proc.setString(2, userId);
                proc.registerOutParameter(3, Types.VARCHAR);
                return proc;
            }
        }, params);
        LOG.debug("jdbc_enable_user result: " + result);
        if ((Integer)result.get("result") != 1)
            throw new AppX("Unable to disable user: " + result.get("v_ret_message"));
    }

    public static void disableUser(EntityManager entityManager, final String userId)
    {
        JdbcTemplate jdbcTemplate = prepareJdbcTemplate(entityManager);
        List<SqlParameter> params = new ArrayList<SqlParameter>();
        params.add(new SqlOutParameter("result", Types.INTEGER));
        params.add(new SqlParameter("EMPE_USERID", Types.VARCHAR));
        params.add(new SqlOutParameter("v_ret_message", Types.VARCHAR));
        Map<String, Object> result = jdbcTemplate.call(new CallableStatementCreator()
        {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException
            {
                String callProcSQL = "{? = call clarity_admin.p_employee.jdbc_disable_user(?, ?)}";
                CallableStatement proc = con.prepareCall(callProcSQL);
                proc.registerOutParameter(1, Types.INTEGER);
                proc.setString(2, userId);
                proc.registerOutParameter(3, Types.VARCHAR);
                return proc;
            }
        }, params);
        LOG.debug("jdbc_disable_user result: " + result);
        if ((Integer)result.get("result") != 1)
            throw new AppX("Unable to disable user: " + result.get("v_ret_message"));
    }

    public static void unlockUser(EntityManager entityManager, final String userId)
    {
        JdbcTemplate jdbcTemplate = prepareJdbcTemplate(entityManager);
        List<SqlParameter> params = new ArrayList<SqlParameter>();
        params.add(new SqlOutParameter("result", Types.INTEGER));
        params.add(new SqlParameter("EMPE_USERID", Types.VARCHAR));
        params.add(new SqlOutParameter("v_ret_message", Types.VARCHAR));
        Map<String, Object> result = jdbcTemplate.call(new CallableStatementCreator()
        {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException
            {
                String callProcSQL = "{? = call sys.p_clty_user_admin.jdbc_unlock_user(?, ?)}";
                CallableStatement proc = con.prepareCall(callProcSQL);
                proc.registerOutParameter(1, Types.INTEGER);
                proc.setString(2, userId);
                proc.registerOutParameter(3, Types.VARCHAR);
                return proc;
            }
        }, params);
        LOG.debug("jdbc_unlock_user result: " + result);
        if ((Integer)result.get("result") != 1)
            throw new AppX("Unable to disable user: " + result.get("v_ret_message"));
    }

    public static String encryptEmailPassword(EntityManager entityManager, final String password)
    {
        JdbcTemplate jdbcTemplate = prepareJdbcTemplate(entityManager);
        List<SqlParameter> params = new ArrayList<SqlParameter>();
        params.add(new SqlOutParameter("result", Types.VARCHAR));
        params.add(new SqlParameter("EMPE_USERID", Types.VARCHAR));
        Map<String, Object> result = jdbcTemplate.call(new CallableStatementCreator()
        {
            @Override
            public CallableStatement createCallableStatement(Connection con) throws SQLException
            {
                String callProcSQL = "{? = call clarity_admin.p_employee.encrypt_password(?)}";
                CallableStatement proc = con.prepareCall(callProcSQL);
                proc.registerOutParameter(1, Types.VARCHAR);
                proc.setString(2, password);
                return proc;
            }
        }, params);
        LOG.debug("encrypt_password result: " + result);
        return (String)result.get("result");
    }

    static class ConnectionCachingDataSource extends AbstractDataSource
    {
        private final EntityManager entityManager;
        private Connection connection;

        ConnectionCachingDataSource(EntityManager entityManager)
        {
            this.entityManager = entityManager;
        }

        @Override
        public Connection getConnection() throws SQLException
        {
            if (connection == null)
            {
                Object delegate = entityManager.getDelegate();
                if (delegate instanceof Session)
                    connection = ((Session)delegate).connection();
                else
                    throw new AppX("We currently support only Hibernate, unknown entity manager delegate: " +
                        delegate.getClass().getName());
            }
            return connection;
        }

        @Override
        public Connection getConnection(String username, String password) throws SQLException
        {
            throw new AppX("getConnection(username, password) method is not supported by this data source.");
        }

        //N.b. This is actually an @Override for a method in 1.7, but does not exist in 1.6
        //During the 1.6->1.7 transition, this will be missing @Override.
        public Logger getParentLogger() throws SQLFeatureNotSupportedException
        {
            return null;
        }

    }
}
