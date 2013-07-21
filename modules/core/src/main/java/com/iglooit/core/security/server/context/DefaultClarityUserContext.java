package com.iglooit.core.security.server.context;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.security.Principal;

/**
 * Base implementation for a {@link MutableClarityUserContext}.
 *
 * @author Michael Truong
 */
public class DefaultClarityUserContext implements MutableClarityUserContext
{
    private static final Log LOG = LogFactory.getLog(DefaultClarityUserContext.class);

    private String poolOracleUser;
    private String backgroundUsername;
    private transient String backgroundUserPassword;

    protected DefaultClarityUserContext()
    {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getTargetDatabaseUsername()
    {
        return poolOracleUser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTargetDatabaseUser(Principal user)
    {
        return StringUtils.equalsIgnoreCase(user.getName(), getTargetDatabaseUsername());
    }

    @Override
    public String getBackgroundUsername()
    {
        return backgroundUsername;
    }

    @Override
    public String getBackgroundUserPassword()
    {
        return backgroundUserPassword;
    }

    @Override
    public void setBackgroundUsername(String username)
    {
        backgroundUsername = username;
    }

    @Override
    public void setBackgroundUserPassword(String password)
    {
        backgroundUserPassword = password;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setTargetDatabaseUsername(String username)
    {
        poolOracleUser = username;
        if (LOG.isDebugEnabled())
        {
            LOG.debug("Database username '" + username + "' is stored in the user context");
        }
    }
}
