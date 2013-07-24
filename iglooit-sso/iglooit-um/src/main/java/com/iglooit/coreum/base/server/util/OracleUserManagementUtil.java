package com.iglooit.coreum.base.server.util;

import com.iglooit.commons.iface.type.AppX;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.core.CallableStatementCreator;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;

import java.nio.charset.Charset;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OracleUserManagementUtil
{
    private static final Log LOG = LogFactory.getLog(OracleUserManagementUtil.class);
    public static final String USER_MANAGER_ROLE = "CO_USER_MANAGER";
    private static final String USER_MANAGER_ROLE_PASSWORD = new String(new byte[]{99, 108, 116, 121, 33, 50, 51, 52},
        Charset.forName("ISO-8859-1"));

    public static JdbcTemplate prepareJdbcTemplate(JdbcTemplate jdbcTemplate)
    {
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

    public static void ossUserCreate(JdbcTemplate jdbc, final String ossUserId, final String ossProfile)
    {
        JdbcTemplate jdbcTemplate = prepareJdbcTemplate(jdbc);
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

    public static void ossSetPasswordAndExpire(JdbcTemplate jdbc, final String ossUserId, final String password)
    {
        JdbcTemplate jdbcTemplate = prepareJdbcTemplate(jdbc);
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

    public static void ossSetPassword(JdbcTemplate jdbc, final String ossUserId, final String password)
    {
        JdbcTemplate jdbcTemplate = prepareJdbcTemplate(jdbc);
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
                String callProcSQL = "{? = call sys.p_clty_user_admin.jdbc_reset_password_dontexpire(?, ?, ?)}";
                CallableStatement proc = con.prepareCall(callProcSQL);
                proc.registerOutParameter(1, Types.INTEGER);
                proc.setString(2, ossUserId);
                proc.setString(3, password);
                proc.registerOutParameter(4, Types.VARCHAR);
                return proc;
            }
        }, params);
        LOG.debug("jdbc_reset_password_dontexpire result: " + result);
        if ((Integer)result.get("result") != 1)
            throw new AppX("Unable to reset user password: " + result.get("p_ret_message"));
    }

    public static void ossChangePassword(JdbcTemplate jdbc, final String ossUserId,
                                                   final String password, final String existingPassword)
    {
        JdbcTemplate jdbcTemplate = prepareJdbcTemplate(jdbc);
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
            throw new AppX("Unable to change user password: " + result.get("p_ret_message"));
    }

    public static void enableUser(JdbcTemplate jdbc, final String userId)
    {
        JdbcTemplate jdbcTemplate = prepareJdbcTemplate(jdbc);
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

    public static void disableUser(JdbcTemplate jdbc, final String userId)
    {
        JdbcTemplate jdbcTemplate = prepareJdbcTemplate(jdbc);
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

    public static void unlockUser(JdbcTemplate jdbc, final String userId)
    {
        JdbcTemplate jdbcTemplate = prepareJdbcTemplate(jdbc);
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
}
