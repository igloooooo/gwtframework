package com.iglooit.core.command.client;

import com.clarity.core.base.iface.command.Request;
import com.clarity.core.base.iface.command.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface CommandServiceClient
{
    <ResponseType extends Response> void run(final Request<ResponseType> request,
                                             final AsyncCallback<ResponseType> innerCallback,
                                             final WrappingCallback<ResponseType> outerCallback);

    <ResponseType extends Response> void run(Request<ResponseType> request, AsyncCallback<ResponseType> response);

    void runList(final List<Request> requests, final AsyncCallback<List<Response>> innerCallback,
                 final WrappingCallback<List<Response>> outerCallback);

    void runList(List<Request> requests, AsyncCallback<List<Response>> innerCallback);
}
