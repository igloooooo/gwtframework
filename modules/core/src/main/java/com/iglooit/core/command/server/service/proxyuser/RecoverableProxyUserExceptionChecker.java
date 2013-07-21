package com.iglooit.core.command.server.service.proxyuser;

import com.clarity.core.command.server.Fn;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang.exception.ExceptionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * A Spring bean that examines a given {@link RuntimeException} to see if this exception is recoverable from a
 * proxy-session-based operation.
 *
 * @author Michael Truong
 */
public class RecoverableProxyUserExceptionChecker
{
    /**
     * Convenient interface for an actual recoverable proxy user exception verification handler.
     *
     * @author Michael Truong
     */
    public interface Verifier extends Fn<Boolean>
    {
        // just to be specific about the return type
    }

    private static final Verifier DEFAULT_VERIFIER = new DefaultRecoverableProxyUserExceptionVerifier();
    private List<Verifier> verifiers = new ArrayList<Verifier>(Arrays.asList(DEFAULT_VERIFIER));

    /**
     * Sets all verifiers to check for recoverable proxy session exceptions.
     *
     * @param verifiers A list of all {@link Verifier} instances. <br>
     *                  Each element is a verifier whose <code>execute</code> method will be run one-by-one in
     *                  the specified order.
     * @deprecated This method is targeted to Spring IoC. Manual addition of custom verifiers should be done by using
     *             {@link #addCustomVerifiers(Verifier...)} method.
     */
    @Deprecated
    public void setVerifiers(List<Verifier> verifiers)
    {
        this.verifiers.clear();
        if (verifiers != null && !verifiers.isEmpty())
        {
            this.verifiers.addAll(verifiers);
        }
    }

    /**
     * Add given custom verifiers into current verifier list.
     *
     * @param verifiers All additional verifiers to be appended to the current verifier list.
     * @return Current instance.
     */
    public RecoverableProxyUserExceptionChecker addCustomVerifiers(Verifier... verifiers)
    {
        if (verifiers != null && verifiers.length > 0)
        {
            this.verifiers.addAll(Arrays.asList(verifiers));
        }
        return this;
    }

    /**
     * Verifies if a given {@link Exception} is a proxy user recoverable exception or not.
     * <p/>
     * Under the hood, all verifiers (implementing interface {@link Verifier}) will be executed and the first
     * <code>true</code> result ends the verification. The {@link Verifier#execute(Object...)} method accepts the
     * following arguments:
     * <ol>
     * <li>The currently checking exception instance.
     * <li>All {@link Throwable} instances in the stacktrace, starting from the checking exception itself.
     * </ol>
     * The {@link Verifier#execute(Object...)} method returns a {@link Boolean} value telling whether the checking
     * exception is recoverable (<code>true</code>) or not (<code>false</code>).
     *
     * @param e Checking exception instance; required.
     * @return <code>true</code> if this given exception is recoverable.
     */
    public boolean isRecoverableException(Exception e)
    {
        Validate.notNull(e, "Checking exception is missing");

        Boolean recoverable = null;
        Throwable[] throwables = ExceptionUtils.getThrowables(e);

        // make sure no more Verifier is added during the verification period
        List<Verifier> unmodifiableVerifiers = null;
        synchronized (verifiers)
        {
            unmodifiableVerifiers = Collections.unmodifiableList(verifiers);
        }

        for (Verifier verifier : unmodifiableVerifiers)
        {
            recoverable = verifier.execute(e, throwables);
            if (Boolean.TRUE.equals(recoverable))
            {
                break;
            }
        }

        return Boolean.TRUE.equals(recoverable);
    }
}
