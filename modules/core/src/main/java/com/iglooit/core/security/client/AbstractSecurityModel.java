package com.iglooit.core.security.client;

import java.util.Map;

import static com.clarity.core.security.client.AbstractSecurityModel.SECURITY_LEVEL.*;

public abstract class AbstractSecurityModel<T extends Enum>
{
    private Map<T, SECURITY_LEVEL> securedFunctions;

    protected AbstractSecurityModel(Map<T, SECURITY_LEVEL> securedFuctions)
    {
        this.securedFunctions = securedFuctions;
        setupDefaultSecurityModel();
    }
    
    protected Map<T, SECURITY_LEVEL> getSecuredFunctions()
    {
        return securedFunctions;
    }
    
    public void makeAccessible(T apiFunction)
    {
        securedFunctions.put(apiFunction, MUTABLE);
    }

    public void makeReadOnly(T apiFunction)
    {
        securedFunctions.put(apiFunction, READ_ONLY);
    }

    public void makeInvisible(T apiFunction)
    {
        securedFunctions.put(apiFunction, INVISIBLE);
    }

    public boolean isAccessible(T function)
    {
        SECURITY_LEVEL level = securedFunctions.get(function);
        return level.equals(MUTABLE);
    }

    public boolean isVisible(T function)
    {
        SECURITY_LEVEL level = securedFunctions.get(function);
        return level.equals(READ_ONLY) || level.equals(MUTABLE);
    }

    public boolean isReadOnly(T function)
    {
        SECURITY_LEVEL level = securedFunctions.get(function);
        return level.equals(READ_ONLY);
    }

    protected abstract void setupDefaultSecurityModel();

    /**
     * The available levels of security for our logical components to be made a top level class.
     */
    protected enum SECURITY_LEVEL
    {
        MUTABLE,
        READ_ONLY,
        INVISIBLE
    }
}
