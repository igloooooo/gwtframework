package com.iglooit.core.command.server.handlers;

import com.clarity.core.base.iface.command.ReadOnlyRequest;
import com.clarity.core.base.iface.command.ReadWriteRequest;
import com.clarity.core.base.iface.command.Request;
import com.clarity.core.base.iface.command.Response;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.springframework.util.StopWatch;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;

/**
 * Utility class for {@link com.clarity.core.base.iface.command.Request}-related operations.
 *
 * @author Michael Truong
 */
public final class RequestHandlingUtil
{
    private static final String UNKNOWN_NAME = "unknown";

    private RequestHandlingUtil()
    {
        // hidden
    }

    /**
     * Returns a display name (for logging, for example) of a given request collection.
     *
     * @param requests All requests whose names will be extracted.
     * @return A text presentation of given requests.
     */
    public static String getRequestBatchDisplayName(Collection<Request<?>> requests)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(requests.size()).append(" requests: {");

        sb.append("size=").append(requests.size());
        for (Request<?> request : requests)
        {
            sb.append(", ").append(request.getClass().getSimpleName());
        }
        sb.append("}");
        return sb.toString();
    }

    /**
     * Returns a display name (for logging, for example) of a given request and optional suffixes.
     *
     * @param request  Object to get display name. If it's <code>null</code>, "unknown" is used as the display name.
     * @param suffixes All optional suffixes which will be appended to the display name.
     * @return A text presentation of the given request.
     */
    public static String getRequestDisplayName(Request<?> request, String... suffixes)
    {
        StringBuilder builder = new StringBuilder(request != null ? request.getClass().getSimpleName() : UNKNOWN_NAME);
        if (!ArrayUtils.isEmpty(suffixes))
        {
            for (String suffix : suffixes)
            {
                builder.append("-").append(suffix);
            }
        }
        return builder.toString();
    }

    /**
     * Returns a display name (for logging, for example) of a given response.
     *
     * @param response Object to get display name. If it's <code>null</code>, "unknown" is used as the display name.
     * @return A text presentation of the given response.
     */
    public static String getResponseDisplayName(Response response)
    {
        return response != null ? response.getClass().getSimpleName() : UNKNOWN_NAME;
    }

    /**
     * Checks if a given request is 'read-only' or not.
     *
     * @param request Object to be checked.
     * @return <code>true</code> if this request is not <code>null</code> and is marked as 'read-only'. Otherwise,
     *         return <code>false</code>.
     */
    public static boolean isReadOnlyRequest(Request<?> request)
    {
        return request != null && request.getClass().isAnnotationPresent(ReadOnlyRequest.class)
            && !request.getClass().isAnnotationPresent(ReadWriteRequest.class);
    }

    /**
     * Checks if a given request is 'read-write' or not.
     *
     * @param request Object to be checked.
     * @return <code>true</code> if this request is not <code>null</code> and is marked as 'read-write'. Otherwise,
     *         return <code>false</code>.
     */
    public static boolean isReadWriteRequest(Request<?> request)
    {
        return request != null && request.getClass().isAnnotationPresent(ReadWriteRequest.class)
            && !request.getClass().isAnnotationPresent(ReadOnlyRequest.class);
    }

    /**
     * Calculates the size in bytes of a given {@link Response} object.
     *
     * @param response Object to calculate size.
     * @return Object size in byte or -1 if given object is <code>null</code> or the size cannot be calculated.
     */
    public static int getResponseSizeInByte(Response response)
    {
        if (response != null)
        {
            ObjectOutputStream ou = null;
            ByteArrayOutputStream baos = null;
            try
            {
                baos = new ByteArrayOutputStream();
                ou = new ObjectOutputStream(baos);
                ou.writeObject(response);

                return baos.size();
            }
            catch (Exception e)
            {
                return -1;
            }
            finally
            {
                IOUtils.closeQuietly(ou);
            }
        }
        return -1;
    }

    /**
     * Prints request execution statistics from a given {@link org.springframework.util.StopWatch} object (which must be used to measure
     * execution time) to a given {@link org.apache.commons.logging.Log}.
     *
     * @param log                A logger which will receive the log message; required.
     * @param stopwatch          {@link org.springframework.util.StopWatch} instance; required.
     * @param requestDisplayName Display name of the request to log.
     */
    public static void printRequestExecutionStatistics(Log log, StopWatch stopwatch, String requestDisplayName)
    {
        if (log == null || stopwatch == null)
        {
            return;
        }

        if (log.isDebugEnabled())
        {
            log.debug(requestDisplayName + " execution statistics -> " + stopwatch.prettyPrint());
        }
        else if (log.isInfoEnabled())
        {
            log.info(requestDisplayName + " execution statistics -> " + stopwatch.shortSummary());
        }
    }
}
