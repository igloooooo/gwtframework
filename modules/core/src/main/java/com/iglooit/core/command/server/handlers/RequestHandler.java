package com.iglooit.core.command.server.handlers;

import com.iglooit.commons.iface.domain.I18NFactoryProvider;
import com.iglooit.commons.iface.domain.Validator;
import com.iglooit.commons.iface.type.AppX;
import com.iglooit.commons.iface.type.Tuple2;
import com.iglooit.core.base.iface.command.*;
import com.iglooit.core.base.iface.domain.DomainEntity;
import com.iglooit.core.oss.iface.command.TypeAheadLoadResponse;
import com.iglooit.core.security.iface.access.domain.FunctionPrivileges.SystemPrivilege;
import com.iglooit.core.security.iface.access.domain.PrivilegeConst;
import com.extjs.gxt.ui.client.data.BaseListLoadResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.exception.ConstraintViolationException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

public abstract class RequestHandler<RequestType extends Request<ResponseType>, ResponseType extends Response>
{
    private static final PrivilegeConst[] PRIVILEGES = {SystemPrivilege.LOGIN};
    private static final Log LOG = LogFactory.getLog(RequestHandler.class);
    private static final OracleErrorMessages OEM = I18NFactoryProvider.get(OracleErrorMessages.class);

    private int totalSuccessfulRequests = 0;
    private int totalFailRequests = 0;
    private long processTimeOfLastRequest = 0;

    private static Set<RequestHandlerCallback> requestHandlerCallbacks = new HashSet<>();

    @Resource
    private RequestHandlerManager requestHandlerManager;

    @PersistenceContext(unitName = "oss")
    private EntityManager em;

    private RequestHandlerRegistration registration;

    @PostConstruct
    protected void registerHandler()
    {
        registration = requestHandlerManager.addRequestHandler(this);
    }

    @PreDestroy
    protected void unRegisterHandler()
    {
        registration.remove();
    }

    protected RequestHandlerManager getRequestHandlerManager()
    {
        return requestHandlerManager;
    }

    public abstract ResponseType handleRequest(RequestType request);

    public final <DE extends DomainEntity> ResponseType handleRequestAndStripResponse(RequestType request)
    {
        long start = System.currentTimeMillis();
        for (RequestHandlerCallback callback : requestHandlerCallbacks)
            callback.onStartProcess(request, this, start);
        try
        {
            //review ms(mg) : we need to chat about the incoming serverside validation
            ResponseType response = handleRequest(request);

            //If we're not a read only request, do a flush to increment any lockVersions on merged entities.z
            if (em != null && request.getClass().getAnnotation(ReadOnlyRequest.class) == null)
            {
                em.flush();
            }

            if (response instanceof DomainEntityResponse)
            {
                stripSingleInstance((DomainEntityResponse<DE>)response);
            }
            else if (response instanceof NullableDomainEntityResponse)
            {
                stripSingleInstance((NullableDomainEntityResponse<DE>)response);
            }
            else if (response instanceof DomainEntityListResponse)
            {
                stripList((DomainEntityListResponse<DE>)response);
            }
            else if (response instanceof DomainEntityTupleResponse)
            {
                stripTuple((DomainEntityTupleResponse)response);
            }
            else if (response instanceof DomainEntityListTuple2Response)
            {
                stripListTuple((DomainEntityListTuple2Response)response);
            }
            else if (response instanceof DomainEntityMapResponse)
            {
                stripMap((DomainEntityMapResponse)response);
            }
            else if (response instanceof ComposedDomainEntityListResponse)
            {
                stripComposedDEList((ComposedDomainEntityListResponse)response);
            }
            else if (response instanceof TypeAheadLoadResponse)
            {
                stripBasePagingLoadResult(((TypeAheadLoadResponse)response));
            }

            // perform any post-handling activity for the created response object prior to returning to the caller
            response.postConstruct();
            postHandleRequest(request, start, System.currentTimeMillis(), true);
            return response;
        }
        catch (Exception ex)
        {
            postHandleRequest(request, start, System.currentTimeMillis(), false);

            if (ex.getCause() instanceof ConstraintViolationException)
            {
                String violationMessage = getConstraintViolationMessage(
                    (ConstraintViolationException)ex.getCause());
                if (violationMessage != null)
                    throw new AppX(violationMessage);
                else
                    throw ex;
            }
            else
                throw ex;
        }
    }

    private void postHandleRequest(Request request, long startTime, long endTime, boolean success)
    {
        processTimeOfLastRequest = endTime - startTime;
        if (success)
            totalSuccessfulRequests++;
        else
            totalFailRequests++;

        for (RequestHandlerCallback callback : requestHandlerCallbacks)
            callback.onCompleteProcess(request, this, endTime, success);
    }

    private String getConstraintViolationMessage(ConstraintViolationException violationException)
    {
        int errorCode = violationException.getSQLException().getErrorCode();

        switch (errorCode)
        {
            case OracleErrorCode.CHILD_RECORD_EXISTS:
                return OEM.getChildRecordExistsMessage();
            case OracleErrorCode.UNIQUE_CONSTRAINT:
                return OEM.getUniqueConstraintMessage();
            default:
                return null;
        }
    }

    private void stripBasePagingLoadResult(TypeAheadLoadResponse response)
    {
        BaseListLoadResult loadingResult = (BaseListLoadResult)response.getResult();
        List data = loadingResult.getData();
        if (data != null && data.size() > 0)
            if (!(data.get(0) instanceof DomainEntity))
                return;

        List<DomainEntity> strippedList = data == null ? null : new DomainEntityStripper().strip(data);
        loadingResult.setData(strippedList);
    }

    private <DE1 extends DomainEntity, DE2 extends DomainEntity> void stripMap(
        DomainEntityMapResponse<DE1, DE2> domainEntityMapResponse)
    {
        Map<DE1, DE2> unstrippedMap = domainEntityMapResponse.getMap();
        Map<DE1, DE2> strippedMap = new HashMap<DE1, DE2>();
        for (Map.Entry<DE1, DE2> entry : unstrippedMap.entrySet())
        {
            DE1 strippedKey = new DomainEntityStripper().strip(entry.getKey());
            DE2 strippedValue = new DomainEntityStripper().strip(entry.getValue());
            strippedMap.put(strippedKey, strippedValue);
        }
        domainEntityMapResponse.setMap(strippedMap);
    }

    private void stripListTuple(DomainEntityListTuple2Response domainEntityListTuple2Response)
    {
        List<Tuple2<DomainEntity, DomainEntity>> list = domainEntityListTuple2Response.getList();
        for (Tuple2<DomainEntity, DomainEntity> tuple2 : list)
        {
            DomainEntity strippedA = new DomainEntityStripper().strip(tuple2.getFirst());
            DomainEntity strippedB = new DomainEntityStripper().strip(tuple2.getSecond());
            tuple2.setFirst(strippedA);
            tuple2.setSecond(strippedB);
        }
    }

    private void stripTuple(DomainEntityTupleResponse domainEntityTupleResponse)
    {
        DomainEntity strippedA = new DomainEntityStripper().strip(domainEntityTupleResponse.getFirst());
        DomainEntity strippedB = new DomainEntityStripper().strip(domainEntityTupleResponse.getSecond());
        domainEntityTupleResponse.setFirst(strippedA);
        domainEntityTupleResponse.setSecond(strippedB);
    }

    private <DE extends DomainEntity> void stripSingleInstance(DomainEntityResponse<DE> domainEntityResponse)
    {
        DE domainEntity = domainEntityResponse.getDomainEntity();
        DE strippedDomainEntity = new DomainEntityStripper().strip(domainEntity);
        domainEntityResponse.setDomainEntity(strippedDomainEntity);
    }

    private <DE extends DomainEntity> void stripSingleInstance(NullableDomainEntityResponse<DE> response)
    {
        DE domainEntity = response.getDomainEntity();
        if (domainEntity == null)
            return;
        DE strippedDomainEntity = new DomainEntityStripper().strip(domainEntity);
        response.setDomainEntity(strippedDomainEntity);
    }

    private <DE extends DomainEntity> void stripList(DomainEntityListResponse<DE> domainEntityListResponse)
    {
        List<DE> unStrippedList = domainEntityListResponse.getList();
        List<DE> strippedList = new DomainEntityStripper().strip(unStrippedList);
        domainEntityListResponse.setList(strippedList);
    }

    private <CDE extends ComposedDomainEntity> void stripComposedDEList(ComposedDomainEntityListResponse<CDE> response)
    {
        List<CDE> strippedList = new DomainEntityStripper().stripComposition(response.getList());
        response.setList(strippedList);
    }

    public boolean validationRequired()
    {
        return true;
    }

    public List<Validator> getAdditionalValidators(RequestType request)
    {
        return new ArrayList<Validator>();
    }

    public abstract PrivilegeConst[] getRequiredPrivileges();

    /**
     * this method should be overidden in request handlers that are used on the server side only. It allows the request
     * to be run without a user associated with it, and skips the logic that checks the privileges.
     * <p/>
     * Note that it is an error to have this set to true, but handle a request from the client. ie client requsets
     * should use
     */
    public boolean privilegeCheckRequired()
    {
        return true;
    }

    public int getTotalRequests()
    {
        return totalSuccessfulRequests + totalFailRequests;
    }

    public int getTotalSuccessfulRequests()
    {
        return totalSuccessfulRequests;
    }

    public int getTotalFailRequests()
    {
        return totalFailRequests;
    }

    public long getProcessTimeOfLastRequest()
    {
        return processTimeOfLastRequest;
    }

    public static void addRequestHandlerCallback(RequestHandlerCallback callback)
    {
        if (callback != null)
            requestHandlerCallbacks.add(callback);
    }

    public static void removeRequestHandlerCallback(RequestHandlerCallback callback)
    {
        if (callback != null)
            requestHandlerCallbacks.remove(callback);
    }
}
