package com.iglooit.server;

import com.iglooit.commons.iface.domain.SystemDateProvider;
import com.iglooit.commons.iface.type.AppX;
import com.iglooit.commons.iface.type.Option;
import com.iglooit.commons.iface.type.Tuple2;
import com.iglooit.commons.iface.um.UserLoginStatus;
import com.iglooit.commons.iface.um.UserPasswordUpdateStatus;
import com.iglooit.commons.iface.um.UserRoleDTO;
import com.iglooit.commons.iface.um.UserSecurityDetailsDTO;
import com.iglooit.commons.server.security.EncryptionProcessor;
import com.iglooit.coreum.account.iface.domain.Individual;
import com.iglooit.coreum.account.iface.domain.Organization;
import com.iglooit.coreum.account.iface.domain.UserRole;
import com.iglooit.coreum.account.server.domain.IndividualHome;
import com.iglooit.coreum.account.server.domain.OrganizationHome;
import com.iglooit.coreum.account.server.domain.SecurityHome;
import com.iglooit.coreum.oss.server.OSSHome;
import com.iglooit.coreum.security.iface.access.domain.FunctionPrivileges.SystemPrivilege;
import com.iglooit.coreum.security.iface.access.domain.Privilege;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.HashSet;
import java.util.Set;

public class UserSecurityServiceImpl implements UserSecurityService
{
    private static final Log LOG = LogFactory.getLog(UserSecurityServiceImpl.class);

    @Resource
    private IndividualHome individualHome;
    @Resource
    private OrganizationHome organizationHome;
    @Resource
    private SecurityHome securityHome;
    @Resource
    private OSSHome ossHome;
    /**
     * Although we accept a {@link javax.sql.DataSource} here, but the data-source injected here must implement {@link
     * javax.sql.DataSource#getConnection(String, String)} properly. Some implementations don't and simply throw an
     * exception instead.
     */
    @Resource(name = "umDataSource")
    private DataSource umDataSource;

    private UserSecurityDetailsDTO doCheckPartyLogin(final String orgName, final String username, final String password)
    {
        String loginPrivilege = SystemPrivilege.LOGIN.getPrivilegeString();
        //Create an empty securityResponse, ready to return
        UserSecurityDetailsDTO response = new UserSecurityDetailsDTO(null, loginPrivilege, null, null);

        //Ensure the user's org exists.
        try
        {
            Organization org = organizationHome.findByName(orgName);
            LOG.debug("Organization loaded from database: " + org);
        }
        catch (AppX nre)
        {
            LOG.error("Could not find organization " + orgName);
            response.setUserLoginStatus(UserLoginStatus.BAD_CREDENTIALS);
            //No organization
            return response;
        }

        //find the username
        //TODO: We assume here that usernames are globally unique! wrong!
        Option<UserRole> userRoleOpt = ossHome.getPartyUserName(username);

        if (userRoleOpt.isNone())
        {
            LOG.error("Could not find user for name: " + username);
            response.setUserLoginStatus(UserLoginStatus.BAD_CREDENTIALS);
            return response;
        }

        UserRole userRole = userRoleOpt.value();
        LOG.debug("User loaded from database: " + userRole);
        String decryptedPassword = EncryptionProcessor.decryptString(userRole, userRole.getPassword());
        final boolean passwordMatchesBSS = password.equals(decryptedPassword);

        response = checkOssSecurityValidation(username, password);

        // throw oracle db errors from here
        if (!response.isGranted())
            return response;

        //unfortunately we have to use JPA to do the casting for us due to the hibernate proxying limitations
        Individual individual = individualHome.getParty(userRole.getParty().getId());
        if (individual == null)
            throw new AppX("login is not an individual");
        boolean employeeNameMatches = individual.getIndividualOrganization().getName().equals(orgName);

        if (!(employeeNameMatches))
        {
            LOG.error("Could not find individual's parent account for name: " + orgName);
            response.setUserLoginStatus(UserLoginStatus.BAD_CREDENTIALS);
            return response;
        }
        //check if user is disabled
        if (!userRole.isActive() || userRole.isDisabledNow(SystemDateProvider.now()))
        {
            LOG.error("User is currently disabled: " + username);
            // disabled, not necessarily locked, however there is no UI difference.
            response.setUserLoginStatus(UserLoginStatus.ACCOUNT_LOCKED);
            return response;
        }
        Privilege priv = securityHome.findPrivilegeByName(loginPrivilege);
        if (!userRole.hasPrivilege(priv))
        {
            LOG.error("Could not find (" + loginPrivilege + ") privilege for individual: " + userRole);
            return new UserSecurityDetailsDTO(UserLoginStatus.NO_LOGIN_PRIVILEGE, priv.getName(), null,
                new UserRoleDTO(userRole.getId().toString(), userRole.getUsername(),
                    userRole.getActiveSecurityRoles()));
        }
        // if the bss password doesn't match, update it to match oss.
        if (!passwordMatchesBSS)
        {
            LOG.info("The password for user " + username + " could not be verified. Trying to sync.");
            boolean syncResult = ossHome.syncBssPassword(username, password);
            if (!syncResult)
            {
                LOG.warn("The password could not be synced. Forcing a password change.");
                response.setUserLoginStatus(UserLoginStatus.PASSWORD_EXPIRED);
                return response;
            }
        }
        response.setUserRole(new UserRoleDTO(userRole.getId().toString(), userRole.getUsername(),
            userRole.getActiveSecurityRoles()));
        response.setPrivilege(priv.getName());
        Set<String> grantedAuthorities = new HashSet<String>();
        response.setAuthorities(grantedAuthorities);

        return response;
    }

    private UserSecurityDetailsDTO checkOssSecurityValidation(String username, String password)
    {
        // create a new connection to the database to check the oracle credentials
        UserSecurityDetailsDTO response = new UserSecurityDetailsDTO(null, null, null, null);
        Connection connectionAsUser = null;
        try
        {
            connectionAsUser = umDataSource.getConnection(username, password);
            if (LOG.isInfoEnabled())
            {
                LOG.info("Checking OSS login credentials - Connected to OSS datasource as user: " +
                    username + ", " + connectionAsUser.toString());
            }
            response.setUserLoginStatus(UserLoginStatus.GRANTED);

            // check warnings
            SQLWarning warnings = connectionAsUser.getWarnings();
            StringBuffer sb = new StringBuffer();
            while (warnings != null)
            {
                String message = warnings.getMessage();
                sb.append(message);
                for (UserLoginStatus code : UserLoginStatus.values())
                {
                    if (code.getErrorCode().isSome() && message.contains(code.getErrorCode().value()))
                    {
                        response.setUserLoginStatus(code);
                        response.setErrorMessage(sb.toString());
                        break;
                    }
                }
                warnings = warnings.getNextWarning();
            }
        }
        catch (SQLException e)
        {
            LOG.error("Could not connect to database as user: " + username + ", " + e.getMessage(), e);
            Tuple2<UserLoginStatus, String> oracleError = getOracleErrorCode(e, 2);
            response.setUserLoginStatus(oracleError.getFirst());
            response.setErrorMessage(oracleError.getSecond());
        }
        finally
        {
            if (connectionAsUser != null)
            {
                try
                {
                    connectionAsUser.close();
                }
                catch (SQLException e)
                {
                    LOG.warn("Could not close database connection as user: " + username, e);
                }
            }
        }
        return response;
    }

    private Tuple2<UserLoginStatus, String> getOracleErrorCode(Throwable e, int searchDepth)
    {
        for (UserLoginStatus code : UserLoginStatus.values())
        {
            if (code.getErrorCode().isSome() && e.getMessage().contains(code.getErrorCode().value()))
                return new Tuple2<UserLoginStatus, String>(code, e.getMessage());
        }
        if (e.getCause() != null && searchDepth > 0)
            return getOracleErrorCode(e.getCause(), searchDepth - 1);
        return new Tuple2<UserLoginStatus, String>(UserLoginStatus.OTHER_ERROR, e.getMessage());
    }

    @Transactional("umTransactionManager")
    @Override
    public UserSecurityDetailsDTO checkPartyLogin(String orgName, String username, String password)
    {
        return doCheckPartyLogin(orgName, username, password);
    }

    @Transactional("umTransactionManager")
    @Override
    public UserPasswordUpdateStatus changePass(String orgName, String username, String password, String newPassword)
    {
        UserSecurityDetailsDTO loginStatus = doCheckPartyLogin(orgName, username, password);
        if (!(loginStatus.isGranted() || loginStatus.isExpired()))
            return UserPasswordUpdateStatus.PASSWORD_INVALID;
        return ossHome.changeUserPassword(username, password, newPassword);
    }

    @Transactional("umTransactionManager")
    @Override
    public UserPasswordUpdateStatus syncPass(String orgName, String username, String newPassword)
    {
        return ossHome.setUserPassword(username, newPassword);
    }
}
