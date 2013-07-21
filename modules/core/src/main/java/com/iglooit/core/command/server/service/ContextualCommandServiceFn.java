package com.iglooit.core.command.server.service;

import com.iglooit.core.base.iface.command.Request;
import com.iglooit.core.base.iface.command.Response;
import com.iglooit.core.command.server.Fn;
import com.iglooit.core.oss.server.LogWrapper;
import com.iglooit.core.oss.server.connectionpool.ConnectionPoolX;
import com.iglooit.core.oss.server.connectionpool.ProxyUserNotFoundX;
import com.iglooit.core.security.iface.domain.PasswordNoLongerValidX;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gwtrpcspring.RemoteServiceUtil;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Observable;

/**
 * Externalized functor for the actual execution of a request handling job. This functor mainly handles the following
 * things: <ul> <li>Check and run a given request with/without proxy session. <li>Check and re-run a given request if
 * "fallback" is enabled and a previous execution failed due to a recoverable error. </ul>
 *
 * @param <IN>  Request type.
 * @param <OUT> Response type.
 * @author Michael Truong
 */
public abstract class ContextualCommandServiceFn<OUT extends Response, IN extends Request<OUT>> extends Observable
    implements Fn<OUT>
{
    private static final Log LOG = LogFactory.getLog(ContextualCommandServiceFn.class);
    private static final LogWrapper LOG_WRAPPER = LogWrapper.getFor(LOG);

    private InternalCommandServiceImpl commandService;

    /**
     * @param ownerCmdService
     */
    public ContextualCommandServiceFn(InternalCommandServiceImpl ownerCmdService)
    {
        this.commandService = ownerCmdService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final OUT execute(Object... args)
    {
        assertExecutionArguments(args);

        @SuppressWarnings("unchecked")
        IN request = (IN)args[0];
        TransactionTemplate txTemplate = (TransactionTemplate)args[1];

        Object[] additionalArgs = extractAdditionalArguments(args);

        // notify observers that this command is about to start
        // Params:
        // [0]: old request
        // [1]: current transaction template
        // [2]: status = "START"
        // [3]: all additional arguments
        setChanged();
        notifyObservers(Arrays.asList(request, txTemplate, "START", additionalArgs));

        try
        {
            return doExecuteInTransaction(txTemplate, request, additionalArgs);
        }
        catch (RuntimeException e)
        {
            LOG_WRAPPER.info(e, "Request failed: " + e.getMessage());

            boolean proxyUserPasswordError = false;
            Throwable ex = e;
            Throwable cause = e.getCause();
            while (cause != null)
            {
                if (cause instanceof ProxyUserNotFoundX && ex instanceof ConnectionPoolX)
                {
                    proxyUserPasswordError = true;
                    break;
                }
                ex = cause;
                cause = ex.getCause();
            }
            if (proxyUserPasswordError)
            {
                try
                {
                    HttpServletRequest httpRequest = RemoteServiceUtil.getThreadLocalRequest();
                    httpRequest.getSession().invalidate();
                }
                catch (Exception e1)
                {
                    LOG.info("An exception occurred while invalidating user's session: " + e1.getMessage(), e);
                }
                throw new PasswordNoLongerValidX("User's password has been changed outside Clarity thin client, " +
                    "forcing the user to re-login.", e);
            }
            throw e;
        }
    }

    private Object[] extractAdditionalArguments(Object... executionArgs)
    {
        Object[] additionalArgs;
        if (executionArgs.length > 2)
        {
            additionalArgs = Arrays.copyOfRange(executionArgs, 2, executionArgs.length);
        }
        else
        {
            additionalArgs = new Object[0];
        }
        return additionalArgs;
    }

    /**
     * Actually executes a given request inside a transaction made by the current {@link org.springframework.transaction.support.TransactionTemplate}. This
     * {@link org.springframework.transaction.support.TransactionTemplate} is passed as an argument of {@link #execute(Object...)} method.
     *
     * @param additionalArgs All additional arguments passed to the {@link ContextualCommandServiceFn} (i.e. all
     *                       <code>args[2..n]</code>).
     * @return Response object.
     */
    protected abstract OUT doExecuteInTransaction(IN request, Object[] additionalArgs);

    OUT doExecuteInTransaction(TransactionTemplate txTemplate, final IN request, final Object[] additionalArgs)
    {
        return txTemplate.execute(new TransactionCallback<OUT>()
        {
            @Override
            public OUT doInTransaction(TransactionStatus transactionStatus)
            {
                return doExecuteInTransaction(request, additionalArgs);
            }
        });
    }

    void assertExecutionArguments(Object... args)
    {
        Assert.notEmpty(args);
        Assert.isTrue(args.length >= 2);
        Assert.isTrue(args[0] instanceof Request<?>);
        Assert.isTrue(args[1] instanceof TransactionTemplate);
    }
}
