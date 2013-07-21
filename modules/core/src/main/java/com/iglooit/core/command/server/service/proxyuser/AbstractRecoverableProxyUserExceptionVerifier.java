package com.iglooit.core.command.server.service.proxyuser;

import com.clarity.core.command.server.service.proxyuser.RecoverableProxyUserExceptionChecker.Verifier;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Base class for all implementations of {@link Verifier} interface. This implementation prepares all necessary
 * information for the actual verification.
 *
 * @author Michael Truong
 */
public abstract class AbstractRecoverableProxyUserExceptionVerifier implements Verifier
{
    private static final Log LOG = LogFactory.getLog(AbstractRecoverableProxyUserExceptionVerifier.class);

    /**
     * {@inheritDoc}
     */
    @Override
    public final Boolean execute(Object... args)
    {
        Validate.isTrue(ArrayUtils.getLength(args) >= 2, "Execution argument list contains less than 2 elements");
        Validate.isTrue(args[0] instanceof Exception, "args[0] is not a " + RuntimeException.class);
        Validate.isTrue(args[1] instanceof Throwable[], "args[1] is not a " + Throwable[].class);

        Exception checkingException = (Exception)args[0];
        Throwable[] throwables = (Throwable[])args[1];

        LOG.debug("Verifying exception [" + checkingException + "] by the verifier " + getClass().getSimpleName());

        boolean recoverable = verify(checkingException, throwables);

        LOG.info("Verifier '" + getClass().getSimpleName() + "' verification result: " + recoverable);
        return recoverable;
    }

    /**
     * Does the verification for a recoverable proxy user exception.
     *
     * @param checkingException Checking exception instance; never <code>null</code>.
     * @param throwables        All {@link Throwable} instances in the stacktrace,
     *                          starting from the checking exception itself;
     *                          never <code>null</code>.
     * @return a bool value telling whether the checking exception is recoverable (<code>true</code>) or not (
     *         <code>false</code>).
     */
    protected abstract boolean verify(Exception checkingException, Throwable[] throwables);
}
