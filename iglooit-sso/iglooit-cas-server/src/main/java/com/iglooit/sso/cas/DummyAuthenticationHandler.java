package com.iglooit.sso.cas;

import org.jasig.cas.authentication.LdapAuthenticationException;
import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.BadCredentialsAuthenticationException;
import org.jasig.cas.authentication.handler.support.AbstractUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentials;
import org.springframework.util.StringUtils;

public class DummyAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler
{
    public DummyAuthenticationHandler()
    {
        log.warn(this.getClass().getName() +
                " is only to be used in a testing environment.  NEVER enable this in a production environment.");
    }

    public boolean authenticateUsernamePasswordInternal(final UsernamePasswordCredentials credentials)
            throws AuthenticationException
    {
        String username = credentials.getUsername();
        String password = credentials.getPassword();

        if (!StringUtils.hasText(username) || !StringUtils.hasText(password))
        {
            log.debug("Both username and password are necessary.");
            return false;
        }

        if (password.contains("expired"))
            throw new LdapAuthenticationException(BadCredentialsAuthenticationException.CODE,
                    "Your password is expired!", "passwordExpired");
        if (password.contains("locked"))
            throw new LdapAuthenticationException(BadCredentialsAuthenticationException.CODE,
                    "Your account is locked!", "accountLocked");
        if (password.contains("disabled"))
            throw new LdapAuthenticationException(BadCredentialsAuthenticationException.CODE,
                    "Your account is disabled!", "accountDisabled");

        if (username.equals(getPasswordEncoder().encode(password)))
        {
            log.debug("User [" + username + "] was successfully authenticated.");
            return true;
        }

        log.debug("User [" + username + "] failed authentication");

        return false;
    }
}
