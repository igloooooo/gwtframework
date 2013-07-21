package com.iglooit.core.iface;

import com.iglooit.commons.iface.type.AppX;
import com.iglooit.core.base.iface.command.Request;
import com.iglooit.core.base.iface.command.Response;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import javax.jws.WebService;
import java.util.List;

@RemoteServiceRelativePath("CommandService")
@WebService
public interface GWTCommandService extends RemoteService
{
    <RT extends Response> RT execute(String cookieValue, Request<RT> request) throws AppX;

    List<Response> execute(String cookieValue, List<Request> requests) throws AppX;

}

