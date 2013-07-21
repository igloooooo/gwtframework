package com.iglooit.core.iface;

import com.clarity.commons.iface.type.AppX;
import com.clarity.core.base.iface.command.Request;
import com.clarity.core.base.iface.command.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.List;

public interface GWTCommandServiceAsync
{
    <RT extends Response> void execute(String cookieValue, Request<RT> request, AsyncCallback<RT> response) throws AppX;

    void execute(String cookieValue, List<Request> requests, AsyncCallback<List<Response>> responses) throws AppX;
    
}
