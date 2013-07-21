package com.iglooit.core.command.client;

import com.clarity.commons.iface.type.Tuple2;
import com.clarity.core.base.iface.command.Request;
import com.clarity.core.base.iface.command.Response;
import com.clarity.core.iface.GWTCommandService;
import com.clarity.core.iface.GWTCommandServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.List;

public class CommandServiceClientImpl implements CommandServiceClient
{
    private final GWTCommandServiceAsync gwtCommandService;

    public CommandServiceClientImpl(GWTCommandServiceAsync serviceGWT)
    {
        this.gwtCommandService = serviceGWT;
    }

    public CommandServiceClientImpl()
    {
        this((GWTCommandServiceAsync)GWT.create(GWTCommandService.class));
    }

    @Override
    public <ResponseType extends Response> void run(final Request<ResponseType> request,
                                                    final AsyncCallback<ResponseType> innerCallback,
                                                    final WrappingCallback<ResponseType> outerCallback)
    {
        Runnable command = new Runnable()
        {
            public void run()
            {
                String jsessionid = Cookies.getCookie("JSESSIONID");
                gwtCommandService.execute(jsessionid, request, outerCallback);
            }
        };
        outerCallback.setInnerCallback(innerCallback);
        outerCallback.setCommand(command);
        command.run();
    }


    @Override
    public <ResponseType extends Response> void run(Request<ResponseType> request,
                                                    AsyncCallback<ResponseType> response)
    {
        run(request, response, new DefaultRequestCallback<ResponseType>());
    }


    @Override
    public void runList(final List<Request> requests,
                        final AsyncCallback<List<Response>> innerCallback,
                        final WrappingCallback<List<Response>> outerCallback)
    {
        Runnable command = new Runnable()
        {
            public void run()
            {
                String jsessionid = Cookies.getCookie("JSESSIONID");
                gwtCommandService.execute(jsessionid, requests, outerCallback);
            }
        };
        outerCallback.setInnerCallback(innerCallback);
        outerCallback.setCommand(command);
        command.run();
    }

    @Override
    public void runList(List<Request> requests, AsyncCallback<List<Response>> innerCallback)
    {
        runList(requests, innerCallback,
            new DefaultRequestCallback<List<Response>>());
    }

    public RequestBatcher getRequestBatcher()
    {
        return new RequestBatcher(this);
    }

    public static class RequestBatcher
    {
        private List<Tuple2<Request, AsyncSuccessCallback>> requests
            = new ArrayList<Tuple2<Request, AsyncSuccessCallback>>();

        private final CommandServiceClientImpl commandService;

        public RequestBatcher(CommandServiceClientImpl commandService)
        {
            this.commandService = commandService;
        }

        public <RequestType extends Request<ResponseType>, ResponseType extends Response>
        void addRequest(RequestType requestType, AsyncSuccessCallback<ResponseType> callback)
        {
            requests.add(new Tuple2<Request, AsyncSuccessCallback>(requestType, callback));
        }

        public void run(final AsyncFailureCallback failureCallback)
        {
            List<Request> reqs = Tuple2.getFirsts(requests);
            commandService.runList(reqs, new AsyncCallback<List<Response>>()
            {
                public void onFailure(Throwable caught)
                {
                    failureCallback.onFailure(caught);
                }

                public void onSuccess(List<Response> result)
                {
                    List<AsyncSuccessCallback> successCallbacks = Tuple2.getSeconds(requests);
                    for (int i = 0; i < result.size(); i++)
                        successCallbacks.get(i).onSuccess(result.get(i));
                }
            });
        }
    }
}
