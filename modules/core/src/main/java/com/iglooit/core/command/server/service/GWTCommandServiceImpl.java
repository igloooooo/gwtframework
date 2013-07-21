package com.iglooit.core.command.server.service;

import com.iglooit.commons.iface.type.AppX;
import com.iglooit.core.base.iface.command.Request;
import com.iglooit.core.base.iface.command.Response;
import com.iglooit.core.base.server.util.HttpUtil;
import com.iglooit.core.command.server.handlers.RequestHandlerManager;
import com.iglooit.core.command.server.handlers.RequestHandlingUtil;
import com.iglooit.core.iface.GWTCommandService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component("commandService")
public class GWTCommandServiceImpl implements GWTCommandService
{
    private static final Log LOG = LogFactory.getLog(GWTCommandServiceImpl.class);

    @Resource
    private RequestHandlerManager requestHandlerManager;

    @Resource
    private InternalCommandServiceImpl internalCommandService;

    @Resource
    private Boolean disableXSRFCheck;

    @Override
    public <RT extends Response> RT execute(String cookieValue, Request<RT> request)
    {
        try
        {
            return executeInner(cookieValue, request);
        }
        catch (RuntimeException e)
        {
            LOG.error("Exception in request: " + request.getClass().getSimpleName() + "\n", e);
            throw e;
        }
    }

    /*
     * batched requests.... if there is an exception in one of them, discard the rest. command pattern from the client
     * side will reinitiate. since the entire list will be transactional, it shouldn't matter if it fails part way
     * through. TODO: ABT(MS): Assuming that commands only write to the local DB, or one in the context of the
     * application transaction
     * 
     * (ms) true; where wouldn't that happen? what needs to be done?
     */
    @Override
    public List<Response> execute(String cookieValue, List<Request> requests)
    {
        String batchName = RequestHandlingUtil.getRequestBatchDisplayName((List)requests);
        LOG.info("Start Batch [" + batchName + "]");

        try
        {
            return executeInner(cookieValue, requests, batchName);
        }
        catch (RuntimeException e)
        {
            LOG.error("Exception in request batch [" + batchName + "]", e);
            throw e;
        }
        finally
        {
            LOG.info("End Batch [" + batchName + "]");
        }
    }

    private List<Response> executeInner(String cookieValue, List<Request> requests, String batchName)
    {
        StopWatch stopwatch = new StopWatch("Batch execution");
        List<Response> responses = new ArrayList<Response>(requests.size());

        // Execute all given requests. If exceptions occur, the execution stops unsuccessfully.
        // NOTE: each request is handled in a separate transaction.
        try
        {
            for (Request<?> request : requests)
            {
                stopwatch.start(RequestHandlingUtil.getRequestDisplayName(request));
                responses.add(executeInner(cookieValue, request));
                stopwatch.stop();
            }
        }
        catch (RuntimeException e)
        {
            throw e;
        }
        finally
        {
            if (stopwatch.isRunning())
            {
                stopwatch.stop();
            }
            RequestHandlingUtil.printRequestExecutionStatistics(LOG, stopwatch, "Batch Request");
        }

        return responses;
    }

    private <RT extends Response> RT executeInner(String cookieValue, Request<RT> request)
    {
        if (LOG.isTraceEnabled())
            LOG.trace(request.getClass().getSimpleName() + " Start");

        // protect against XSRF
        // http://groups.google.com/group/Google-Web-Toolkit/web/security-for-gwt-applications?pli=1
        String serverCookieValue = HttpUtil.getSessionCookieFromServerSide();
        if (cookieValue != null && !cookieValue.equals(serverCookieValue))
        {
            // we have ourselves a potential XSRF
            if (requestHandlerManager.requestNeedsXSRFCheck(request.getClass()))
            {
                LOG.error("SECURITY: invalid request received, session cookie does not match the " +
                    "inbound session cookie. " + HttpUtil.getRequestLoggingInfo());

                if (!disableXSRFCheck)
                {
                    throw new AppX("Invalid request. please check log for details");
                }
                else
                {
                    LOG.warn("XRSF security check is disabled.  Client cookie is '" + cookieValue +
                        "', but server cookie is '" + serverCookieValue + "'");
                }
            }
        }
        else if (cookieValue == null)
        {
            if (serverCookieValue != null)
            {
                LOG.error("SECURITY: invalid request received, there is no session established " +
                    HttpUtil.getRequestLoggingInfo());
                throw new AppX("Invalid request. please check log for details");
            }
            else
            {
                LOG.info("No server side cookie, assuming web service request");

                LOG.error("SECURITY: No server side session. Spring Security failure?");
                throw new AppX("Invalid request. please check log for details");
            }
        }

        return internalCommandService.executeInner(request);
    }
}
