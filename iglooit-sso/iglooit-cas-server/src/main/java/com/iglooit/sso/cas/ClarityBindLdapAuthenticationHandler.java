package com.iglooit.sso.cas;

import com.iglooit.commons.iface.type.Option;
import com.iglooit.commons.iface.um.UserPasswordUpdateStatus;
import com.iglooit.commons.iface.um.UserSecurityDetailsDTO;
import com.iglooit.coreum.account.iface.domain.UserRole;
import com.iglooit.coreum.account.server.domain.OrganizationHome;
import com.iglooit.coreum.oss.server.OSSHome;
import com.iglooit.um.server.UserSecurityService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.adaptors.ldap.BindLdapAuthenticationHandler;
import org.jasig.cas.authentication.principal.Credentials;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;

import javax.annotation.Resource;

public class ClarityBindLdapAuthenticationHandler extends BindLdapAuthenticationHandler
{
    private static final Log LOG = LogFactory.getLog(ClarityBindLdapAuthenticationHandler.class);

    @Resource(name = "defaultOrganization")
    private String defaultOrganization;

    @Resource
    private UserSecurityService securityService;

    @Resource
    private OrganizationHome organizationHome;

    @Resource
    private OSSHome ossHome;

    @Override
    protected boolean postAuthenticate(final Credentials credentials,
                                       final boolean authenticated)
    {
        if (!authenticated)
            return false;
        if (credentials instanceof UsernamePasswordCredentials)
        {
            // TODO HA: Get the organization from LDAP credentials
            String orgName = defaultOrganization;
            String username = ((UsernamePasswordCredentials)credentials).getUsername();
            String password = ((UsernamePasswordCredentials)credentials).getPassword();
            UserSecurityDetailsDTO result = securityService.checkPartyLogin(orgName, username, password);
            if (result.isGranted())
            {
                LOG.debug("The password is in sync.");
                return true;
            }

            LOG.info("Now syncing the password.");
            Option<UserRole> userRoleOpt = ossHome.getPartyUserName(username);
            if (userRoleOpt.isNone())
            {
                LOG.warn("Could not find user for name: " + username);
                return false;
            }
            UserRole userRole = userRoleOpt.value();
            LOG.debug("User role loaded: " + userRole);

            UserPasswordUpdateStatus updateStatus = securityService.syncPass(defaultOrganization, username, password);
            if (updateStatus != UserPasswordUpdateStatus.PASSWORD_VALID)
            {
                LOG.warn("Sync failed, because password update wasn't successful: " + result);
                return false;
            }
            LOG.debug("Now checking to see if the user can log in with the new password.");
            result = securityService.checkPartyLogin(defaultOrganization, username, password);
            if (result.isGranted())
            {
                LOG.debug("Sync is successful now.");
                return true;
            }
            else
            {
                LOG.warn("Still can't authenticate the user after sync: " + result);
                return false;
            }
        }
        else
        {
            LOG.warn("Credentials " + credentials + "was not an instance of UsernamePasswordCredentials.");
            return false;
        }
    }
}
