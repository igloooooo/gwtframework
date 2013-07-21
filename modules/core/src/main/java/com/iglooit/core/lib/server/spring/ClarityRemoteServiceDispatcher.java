package com.iglooit.core.lib.server.spring;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.gwtrpcspring.RemoteServiceDispatcher;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class ClarityRemoteServiceDispatcher extends RemoteServiceDispatcher
{
    private static final Log LOG = LogFactory.getLog(ClarityRemoteServiceDispatcher.class);
    private static final String GENERIC_FAILURE_MSG = "The call failed on the server, " +
            "please contact your system administrator for assistance with the following reference id {%s}";

    @Override
    protected void doUnexpectedFailure(Throwable throwable)
    {
        String refId = UUID.randomUUID().toString();
        LOG.error("Error on dispatching RPC call {" + refId + "}: ", throwable);

        try
        {
            getThreadLocalResponse().reset();
        }
        catch (IllegalStateException ex)
        {
            /*
            * If we can't reset the request, the only way to signal that something
            * has gone wrong is to throw an exception from here. It should be the
            * case that we call the user's implementation code before emitting data
            * into the response, so the only time that gets tripped is if the object
            * serialization code blows up.
            */
            throw new RuntimeException("Unable to report failure", throwable);
        }
        ServletContext servletContext = getServletContext();
        writeResponseForUnexpectedFailure(servletContext,
                getThreadLocalResponse(), refId, throwable);
    }

    private void writeResponseForUnexpectedFailure(ServletContext servletContext,
                                                   HttpServletResponse response,
                                                   String referenceId, Throwable failure)
    {
        servletContext.log("Exception while dispatching incoming RPC call", failure);
        // Send GENERIC_FAILURE_MSG with 500 status.
        //
        try
        {
            response.setContentType("text/plain");
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try
            {
                response.getOutputStream().write(String.format(GENERIC_FAILURE_MSG, referenceId).getBytes("UTF-8"));
            }
            catch (IllegalStateException e)
            {
                // Handle the (unexpected) case where getWriter() was previously used
                response.getWriter().write(GENERIC_FAILURE_MSG);
            }
        }
        catch (IOException ex)
        {
            servletContext.log(
                    "respondWithUnexpectedFailure failed while sending the previous failure to the client",
                    ex);
        }
    }


}
