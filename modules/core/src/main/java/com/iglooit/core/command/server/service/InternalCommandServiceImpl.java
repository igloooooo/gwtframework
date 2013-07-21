package com.iglooit.core.command.server.service;

import com.iglooit.commons.iface.domain.ValidationResult;
import com.iglooit.commons.iface.type.AppX;
import com.iglooit.commons.iface.type.Option;
import com.iglooit.commons.iface.util.StringUtil;
import com.iglooit.core.base.iface.command.*;
import com.iglooit.core.base.iface.domain.Validatable;
import com.iglooit.core.base.iface.domain.ValidationX;
import com.iglooit.core.base.server.util.HttpUtil;
import com.iglooit.core.base.server.util.RecursiveReflectionToStringBuilder;
import com.iglooit.core.command.server.Fn;
import com.iglooit.core.command.server.VoidFn;
import com.iglooit.core.command.server.handlers.RequestHandler;
import com.iglooit.core.command.server.handlers.RequestHandlerManager;
import com.iglooit.core.command.server.handlers.RequestHandlingUtil;
import com.iglooit.core.command.server.service.proxyuser.RecoverableProxyUserExceptionChecker;
import com.iglooit.core.iface.SecurityService;
import com.iglooit.core.lib.server.security.impl.SecurityHome;
import com.iglooit.core.oss.server.TransactionUtil;
import oracle.ucp.jdbc.PoolDataSource;
import org.apache.commons.lang.builder.ToStringStyle;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.StopWatch;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InternalCommandServiceImpl
{
    private static final Log LOG = LogFactory.getLog(InternalCommandServiceImpl.class);

    @Resource
    private SecurityHome securityHome;

    @Resource
    private SecurityService securityService;

    @Resource
    private JpaTransactionManager ossTxManager;

    @Resource
    private RequestHandlerManager requestHandlerManager;

    @Resource(name = "ossDataSource")
    private PoolDataSource poolDataSource;

    @Resource
    private RecoverableProxyUserExceptionChecker proxyUserExceptionChecker;

    private Fn<Response> commandServiceFunctor = new ContextualCommandServiceFn<Response, Request<Response>>(this)
    {
        @Override
        protected Response doExecuteInTransaction(Request<Response> request, Object[] additionalArgs)
        {
            StopWatch stopWatch = (StopWatch)additionalArgs[0];
            return executeInnerInTransaction(request, stopWatch);
        }
    };

    Fn<Response> getCommandServiceFunctor()
    {
        return commandServiceFunctor;
    }

    /**
     * @deprecated This method is used for testing purpose only! Never use it in any other context.
     */
    @Deprecated
    void setCommandServiceFunctor(Fn<Response> fn)
    {
        this.commandServiceFunctor = fn;
    }

    /**
     * Executes a given request.
     *
     * @return Request execution result.
     */
    @SuppressWarnings("unchecked")
    public <RT extends Response> RT executeInner(final Request<RT> request)
    {
        String requestDisplayName = RequestHandlingUtil.getRequestDisplayName(request);
        StopWatch stopwatch = new StopWatch(requestDisplayName);

        printRequestExecutionStartup(request);
        try
        {
            TransactionTemplate ossTxTemplate = new TransactionTemplate(ossTxManager);
            ossTxTemplate.setReadOnly(true);
            ossTxTemplate.setPropagationBehavior(TransactionDefinition.PROPAGATION_SUPPORTS);
            ossTxTemplate.setName(RequestHandlingUtil.getRequestDisplayName(request, "outer"));

            RT response = (RT)getCommandServiceFunctor().execute(request, ossTxTemplate, stopwatch);
            printRequestExecutionEnding(response);
            return response;
        }
        finally
        {
            if (stopwatch.isRunning())
            {
                stopwatch.stop();
            }
            RequestHandlingUtil.printRequestExecutionStatistics(LOG, stopwatch, requestDisplayName);
        }
    }

    private <RT extends Response> RT executeInnerInTransaction(Request<RT> request, StopWatch stopwatch)
    {
        String requestDisplayName = RequestHandlingUtil.getRequestDisplayName(request);
        RequestHandler<Request<RT>, RT> handler = requestHandlerManager.getHandler(request);

        RT response = null;
        if (request.getClass().getAnnotation(NoSessionCheckingRequest.class) != null)
        {
            /*
             * The following block is a hack of privilege security check for fsm birt report JIRA - TFT-888: create
             * security hole for birt reports
             * 
             * Reason: Here we have web services defined to handler request from birt side in order to populate data in
             * viewer(in birt), however the request send from birt side doesn't come with a session (stateless) so
             * cannot pass privilege check when requestHandler handler those request. So we introduce request annotation
             * "NoSessionCheckingRequest" here for all request used to get data for bss web services used by birt
             */
            response = handleRequestWithoutSessionCheck(request, handler, stopwatch);
        }
        else
        {
            response = handleNormalRequest(request, requestDisplayName, handler, stopwatch);
        }

        return response;
    }

    private void printRequestExecutionEnding(Response response)
    {
        if (LOG.isTraceEnabled())
        {
            String responseToString = new RecursiveReflectionToStringBuilder(response, ToStringStyle.SIMPLE_STYLE)
                .toString();
            LOG.trace(RequestHandlingUtil.getResponseDisplayName(response) + " - Result data: " + responseToString);
        }
    }

    private void printRequestExecutionStartup(Request<?> request)
    {
        if (LOG.isTraceEnabled())
        {
            String requestToString = new RecursiveReflectionToStringBuilder(request, ToStringStyle.SIMPLE_STYLE)
                .toString();
            LOG.trace(requestToString + " - Start");
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private <RT extends Response> RT handleNormalRequest(Request<RT> request, String requestDisplayName,
                                                         RequestHandler<Request<RT>, RT> handler, StopWatch stopwatch)
    {
        preProcessRequestBeforeExecuting(request, (RequestHandler)handler, stopwatch);

        stopwatch.start("[Normal request execution] Actual request execution");
        if (LOG.isTraceEnabled())
        {
            LOG.trace("entering, handler class: " + handler.getClass().getSimpleName());
        }

        // Determine if we are annotated as a read-only request, and can use non-transactional semantics in
        // txManager
        RT response;
        if (request.getClass().getAnnotation(ReadOnlyRequest.class) != null)
        {
            LOG.debug("Handling read-only request: " + requestDisplayName);
            response = executeInnerReadOnly(handler, request, RequestHandlingUtil.isReadOnlyRequest(request));
        }
        else
        {
            LOG.debug("Handling read-write request: " + requestDisplayName);
            response = executeInnerReadWrite(handler, request, RequestHandlingUtil.isReadWriteRequest(request));
        }
        stopwatch.stop();

        stopwatch.start("[Normal request execution] Response result logging");
        if (LOG.isDebugEnabled())
        {
            int responseSize = RequestHandlingUtil.getResponseSizeInByte(response);
            LOG.debug("Size: " + responseSize + " - " + RequestHandlingUtil.getResponseDisplayName(response));
        }

        stopwatch.stop();

        return response;
    }

    private void preProcessRequestBeforeExecuting(final Request<?> request,
                                                  final RequestHandler<Request<?>, ?> handler,
                                                  final StopWatch stopwatch)
    {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition(
            TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        def.setReadOnly(true);
        def.setName(RequestHandlingUtil.getRequestDisplayName(request, "pre-check"));
        TransactionUtil.doInTransaction(ossTxManager, def, new VoidFn()
        {
            @Override
            protected void doExecute(Object... args)
            {

                // compromise to handle the fact that an option is never returned when there is no active individual
                stopwatch.start("[Normal request execution] Active individual existence check");
                boolean hasActiveIndividual = isActiveIndividualPresented();
                if (!handler.privilegeCheckRequired() && hasActiveIndividual)
                {
                    throw new AppX("Request Handlers that interface with external systems " +
                        "should always have privilege checks enabled - offending handler: " +
                        handler.getClass().getName());
                }
                stopwatch.stop();

                stopwatch.start("[Normal request execution] Request privilege check");
                if (handler.privilegeCheckRequired())
                {
                    securityHome.checkPrivileges(HttpUtil.getServletRequest(), handler.getRequiredPrivileges());
                }
                stopwatch.stop();

                // validation catchall
                stopwatch.start("[Normal request execution] Request validation");
                validateRequestBeforeExecuting(request, handler);
                stopwatch.stop();

            }
        });
    }

    private <RT extends Response> void validateRequestBeforeExecuting(final Request<?> request,
                                                                      final RequestHandler<Request<?>, ?> handler)
    {
        if (handler.validationRequired() && request instanceof ValidatableRequest)
        {
            Map<Validatable, List<ValidationResult>> results = new HashMap<Validatable, List<ValidationResult>>();
            boolean errorsFound = false;
            for (Validatable validatable : ((ValidatableRequest)request).getValidatables())
            {
                if (validatable != null)
                {
                    List<ValidationResult> vr = new ArrayList<ValidationResult>();
                    // add domain entity standard validation
                    vr.addAll(validatable.validate(Option.<String>none()));
                    // add any additional validators that the request handler specifies
                    vr.addAll(validatable.validate(Option.<String>none(), handler.getAdditionalValidators(request)));
                    if (vr.size() > 0)
                        errorsFound = true;
                    results.put(validatable, vr);
                }
            }
            if (errorsFound)
            {
                for (Map.Entry<Validatable, List<ValidationResult>> entry : results.entrySet())
                {
                    if (entry.getValue().size() > 0)
                        if (LOG.isInfoEnabled())
                            LOG.info("Validation errors found for: " + entry.getKey().getClass().getSimpleName() + " " +
                                entry.getKey());

                    for (ValidationResult validationResult : entry.getValue())
                        if (LOG.isInfoEnabled())
                            LOG.info("Validation error: " + "tags: (" +
                                StringUtil.join(", ", validationResult.getTags()) + ") " + "reason: " +
                                validationResult.getReason());

                }
                throw new ValidationX("Validation failed for request", results);
            }
        }
    }

    private boolean isActiveIndividualPresented()
    {
        boolean hasActiveIndividual;
        try
        {
            // compromise to handle the fact that an option is never returned when there is no active individual
            hasActiveIndividual = securityService.getActiveIndividualWeak().isSome();
        }
        catch (AppX x)
        {
            hasActiveIndividual = false;
        }
        return hasActiveIndividual;
    }

    private <RT extends Response> RT handleRequestWithoutSessionCheck(Request<RT> request,
                                                                      RequestHandler<Request<RT>, RT> handler,
                                                                      StopWatch stopwatch)
    {
        stopwatch.start("[Request handling without session check] Actual request execution");

        LOG.debug("Handling non session checking request: " + RequestHandlingUtil.getRequestDisplayName(request));
        RT rt = executeInnerWithTransactionType(handler, request,
            !request.getClass().isAnnotationPresent(ReadWriteRequest.class));

        stopwatch.stop();

        return rt;
    }

    // /Intercept correct method based on pointcut in spring config (read only vs read write)
    public <RT extends Response> RT executeInnerReadOnly(final RequestHandler<Request<RT>, RT> handler,
                                                         final Request<RT> request, boolean ossReadWrite)
    {
        return executeInnerWithTransactionType(handler, request, true);

    }

    public <RT extends Response> RT executeInnerReadWrite(final RequestHandler<Request<RT>, RT> handler,
                                                          final Request<RT> request, boolean ossReadWrite)
    {
        return executeInnerWithTransactionType(handler, request, false);
    }

    public <RT extends Response> RT executeInnerWithTransactionType(final RequestHandler<Request<RT>, RT> handler,
                                                                    final Request<RT> request, boolean readOnlyTrans)
    {
        DefaultTransactionDefinition def = new DefaultTransactionDefinition();
        def.setReadOnly(readOnlyTrans);
        def.setName(RequestHandlingUtil.getRequestDisplayName(request, "main"));
        return TransactionUtil.doInTransaction(ossTxManager, def, new Fn<RT>()
        {
            @Override
            public RT execute(Object... args)
            {
                return handler.handleRequestAndStripResponse(request);
            }
        });
    }
}
