package com.iglooit.core.command.server.handlers;

import com.clarity.commons.iface.type.AppX;
import com.clarity.core.base.iface.command.Request;
import com.clarity.core.base.iface.command.Response;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Component
public class RequestHandlerManager
{
    private static final Log LOG = LogFactory.getLog(RequestHandlerManager.class);
    private final Set<Class> requestsThatDontNeedXSRFCheck = new HashSet<Class>();

    private Map<String, RequestHandler> requestHandlers;


    public RequestHandlerManager()
    {
        requestHandlers = new HashMap<String, RequestHandler>();
    }

    public void addRequestThatDoesntNeedToBeCheckedForXSRF(Class request)
    {
        if (!Request.class.isAssignableFrom(request))
            throw new AppX(request.getSimpleName() + " should be a subclass of Request");
        requestsThatDontNeedXSRFCheck.add(request);
    }

    public boolean requestNeedsXSRFCheck(Class requestHandlerClass)
    {
        //check that the request is not in our whitelisted setof allowed requests
        return !requestsThatDontNeedXSRFCheck.contains(requestHandlerClass);
    }


    public <RT extends Response> RequestHandler<Request<RT>, RT> getHandler(Request<RT> request)
    {
        String key = getHandlerKey(request.getClass());
        if (!requestHandlers.containsKey(key))
        {
            throw new AppX("Request type not handled: " + key);
        }
        return requestHandlers.get(key);
    }

    public <RequestType extends Request<ResponseType>, ResponseType extends Response>
    ResponseType handleRequest(RequestType request)
    {
        ResponseType response = getHandler(request).handleRequest(request);

        // perform any post-handling activity for the created response object prior to returning to the caller
        response.postConstruct();

        return response;
    }

    private <RequestType extends Request<ResponseType>, ResponseType extends Response>
    String getHandlerKey(Class<RequestType> requestClass)
    {
        return requestClass.getName();
    }


    private <RequestType extends Request<ResponseType>, ResponseType extends Response>
    Class<RequestType> getRequestClass(RequestHandler<RequestType, ResponseType> requestHandler)
    {
        Class<RequestType> result = getRequestTypeFromGenerics(requestHandler);
        if (result == null)
            throw new AppX("Unable to determine RequestType for requestHandler = " + this.getClass().getName() + ". " +
                " Check that the RequestHandler class has its generic parameters correcly specified");
        else
            return result;
    }

    public <RequestType extends Request<ResponseType>, ResponseType extends Response>
    RequestHandlerRegistration addRequestHandler(RequestHandler<RequestType, ResponseType> requestHandler)
    {
        Class requestClass = getRequestClass(requestHandler);
        if (LOG.isInfoEnabled()) LOG.debug("Adding request handler for class: " + requestClass.getSimpleName());
        return addRequestHandler(requestClass, requestHandler);
    }

    public <RequestType extends Request<ResponseType>, ResponseType extends Response>
    void removeRequestHandler(RequestHandler<RequestType, ResponseType> requestHandler)
    {
        Class<RequestType> requestClass = getRequestClass(requestHandler);
        String handlerKey = getHandlerKey(requestClass);
        if (!requestHandlers.containsKey(handlerKey))
            throw new AppX("Request handler removing non existant handler!");
        requestHandlers.remove(handlerKey);
    }

    private <RequestType extends Request<ResponseType>, ResponseType extends Response>
    RequestHandlerRegistration addRequestHandler(Class<RequestType> requestClass,
                                                 RequestHandler<RequestType, ResponseType> handler)
    {
        String key = getHandlerKey(requestClass);
        if (requestHandlers.containsKey(key))
        {
            throw new AppX("Request type already handled: " + key +
                " by handler of class: " + requestHandlers.get(key).getClass());
        }
        requestHandlers.put(key, handler);
        return new RequestHandlerRegistration(handler, this);
    }

    /**
     * for references:
     * http://www.artima.com/weblogs/viewpost.jsp?thread=208860
     * http://tutorials.jenkov.com/java-reflection/generics.html
     *
     * @param requestHandler
     * @return
     */
    private Class getRequestTypeFromGenerics(RequestHandler requestHandler)
    {
        for (Class c = requestHandler.getClass(); c != Object.class; c = c.getSuperclass())
        {
            if (LOG.isTraceEnabled()) LOG.trace("Class: " + c.getName());
            Type type = c.getGenericSuperclass();
            if (type instanceof ParameterizedType)
            {
                ParameterizedType parameterizedType = (ParameterizedType)type;
                if (LOG.isTraceEnabled()) LOG.trace("type: " + parameterizedType);
                Type[] typeArgs = parameterizedType.getActualTypeArguments();
                for (Type typeArg : typeArgs)
                {
                    Class typeArgClass = (Class)typeArg;
                    if (Request.class.isAssignableFrom(typeArgClass))
                    {
                        if (LOG.isTraceEnabled()) LOG.trace("FOUND: " + typeArgClass);
                        return typeArgClass;
                    }
                }
            }
        }
        return null;
    }
}
