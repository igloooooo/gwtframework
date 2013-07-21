package com.iglooit.core.security.server.context;

import java.security.Principal;

/**
 * An extended version of {@link MutableClarityUserContext} which supports modification of contextual information.
 *
 * @author Michael Truong
 */
public interface MutableClarityUserContext
{
    /**
     * Returns the username which is used as the username to create database connections. This is basically the
     * information configured at runtime.
     */
    String getTargetDatabaseUsername();

    /**
     * Checks if a given principal is the one who connects to the actual database, i.e. pool user (typically
     * CLARITY_POOL).
     *
     * @param user User object to check; required.
     */
    boolean isTargetDatabaseUser(Principal user);

    String getBackgroundUsername();

    String getBackgroundUserPassword();

    /**
     * Sets a new username which is used as the username to create database connections. This method should be called
     * ONCE after the application starts.
     */
    void setTargetDatabaseUsername(String username);

    void setBackgroundUsername(String username);

    void setBackgroundUserPassword(String password);
}
