package com.iglooit.core.command.server.service.proxyuser;

import com.clarity.core.oss.server.SimplePatternMatcher;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Michael Truong
 */
public class ErrorMessageRecoverableProxyUserExceptionVerifier extends AbstractRecoverableProxyUserExceptionVerifier
{
    private static final String[] DEFAULT_PATTERNS =
        {
            "*ORA-01031*", // insufficient privileges
            "*ORA-00942*does not exist*", // table/view unexists
            "*PLS-*identifier*must be declared*" // unknown PL/SQL function/proc
        };

    private List<String> validPatterns = new LinkedList<String>(Arrays.asList(DEFAULT_PATTERNS));

    /**
     * Sets a new valid pattern list for this verifier. This list overwrites the default setting.
     *
     * @param validPatterns The new value to set.
     */
    public void setValidPatterns(List<String> validPatterns)
    {
        this.validPatterns.clear();
        if (validPatterns != null && !validPatterns.isEmpty())
        {
            this.validPatterns.addAll(validPatterns);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean verify(Exception checkingException, Throwable[] throwables)
    {
        boolean recoverable = false;

        String[] localValidPatterns = null;
        synchronized (validPatterns)
        {
            localValidPatterns = validPatterns.toArray(new String[validPatterns.size()]);
        }

        for (Throwable throwable : throwables)
        {
            String errorMessage = throwable.getMessage();
            if (SimplePatternMatcher.match(localValidPatterns, errorMessage))
            {
                recoverable = true;
                break;
            }
        }

        return recoverable;
    }

}
