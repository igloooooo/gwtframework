package com.iglooit.core.command.server.service.proxyuser;

import com.clarity.core.oss.server.connectionpool.ProxySessionNotGrantedX;
import com.clarity.core.oss.server.connectionpool.ProxyUserNotFoundX;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * The default verifier which relies on the root exception type extracted from a given exception.
 * <p/>
 * Without any custom valid exception set (i.e. via {@link #setValidExceptions(java.util.List)}), the
 * {@link #DEFAULT_VALID_EXCEPTIONS} are used as the default valid exception list. Using
 * {@link #setValidExceptions(java.util.List)} overwrites the default setting.
 *
 * @author Michael Truong
 */
public class DefaultRecoverableProxyUserExceptionVerifier extends AbstractRecoverableProxyUserExceptionVerifier
{
    private static final Class<?>[] DEFAULT_VALID_EXCEPTIONS = new Class<?>[]
        {
            ProxyUserNotFoundX.class, // proxy user doesn't exist with provided username/password
            ProxySessionNotGrantedX.class // proxy user has no permission to connect as another user
        };

    private List<Class<?>> validExceptions = new LinkedList<Class<?>>(Arrays.asList(DEFAULT_VALID_EXCEPTIONS));

    /**
     * Sets a new valid exception list for this verifier. This list overwrites the default setting.
     *
     * @param validExceptions
     */
    public void setValidExceptions(List<Class<?>> validExceptions)
    {
        this.validExceptions.clear();
        if (validExceptions != null && !validExceptions.isEmpty())
        {
            this.validExceptions.addAll(validExceptions);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean verify(Exception checkingException, Throwable[] throwables)
    {
        boolean recoverable = false;

        List<Class<?>> localValidExceptions = null;
        synchronized (validExceptions)
        {
            localValidExceptions = Collections.unmodifiableList(validExceptions);
        }

        for (Throwable cause : throwables)
        {
            for (Class<?> baseClass : localValidExceptions)
            {
                if (baseClass.isAssignableFrom(cause.getClass()))
                {
                    recoverable = true;
                    break;
                }
            }
        }

        return recoverable;
    }
}
