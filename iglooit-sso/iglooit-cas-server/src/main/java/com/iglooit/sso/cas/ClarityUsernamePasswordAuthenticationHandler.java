package com.iglooit.sso.cas;

import com.iglooit.commons.iface.um.UserLoginStatus;
import com.iglooit.commons.iface.um.UserSecurityDetailsDTO;
import com.iglooit.um.server.UserSecurityService;
import org.jasig.cas.authentication.LdapAuthenticationException;
import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.BadCredentialsAuthenticationException;
import org.jasig.cas.authentication.handler.BadUsernameOrPasswordAuthenticationException;
import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;

import javax.annotation.Resource;

public class ClarityUsernamePasswordAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler
{
    @Resource(name = "defaultOrganization")
    private String defaultOrganization;

    @Resource
    private UserSecurityService securityService;

    public boolean authenticateUsernamePasswordInternal(final UsernamePasswordCredentials credentials)
        throws AuthenticationException
    {
        String username = credentials.getUsername();
        String password = credentials.getPassword();

        String orgName;
        String[] organizationIndividual = username.split("/");
        if (organizationIndividual.length >= 2)
        {
            orgName = organizationIndividual[0];
            username = organizationIndividual[1];
        }
        else if (defaultOrganization != null && defaultOrganization.length() > 0)
            orgName = defaultOrganization;
        else
            throw new BadUsernameOrPasswordAuthenticationException();
        UserSecurityDetailsDTO userSecurityDetails =
            securityService.checkPartyLogin(orgName, username, password);
        log.debug("Authentication of [" + username + "] resulted in [" + userSecurityDetails + "]");
        if (userSecurityDetails.getUserLoginStatus() == UserLoginStatus.ACCOUNT_LOCKED)
            throw new LdapAuthenticationException(BadCredentialsAuthenticationException.CODE,
                "Your account is locked!", "accountLocked");
        if (userSecurityDetails.getUserLoginStatus() == UserLoginStatus.PASSWORD_EXPIRED)
            throw new LdapAuthenticationException(BadCredentialsAuthenticationException.CODE,
                "Your password is expired!", "passwordExpired");
        return userSecurityDetails.isGranted();
    }
}
