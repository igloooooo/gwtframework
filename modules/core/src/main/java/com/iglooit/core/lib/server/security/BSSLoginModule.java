package com.iglooit.core.lib.server.security;

import javax.security.auth.Subject;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.spi.LoginModule;
import java.util.Map;

public class BSSLoginModule implements LoginModule
{
    private Subject subject;

    public BSSLoginModule()
    {
    }

    public boolean abort() throws javax.security.auth.login.LoginException
    {
        return true;
    }

    public boolean commit() throws javax.security.auth.login.LoginException
    {
        return true;
    }

    public void initialize(Subject subject,
                           CallbackHandler callbackHandler,
                           Map<String, ?> stringMap,
                           Map<String, ?> stringMap1)
    {
    }

    public boolean login() throws javax.security.auth.login.LoginException
    {
        return true;
    }

    public boolean logout() throws javax.security.auth.login.LoginException
    {
        return true;
    }
}
