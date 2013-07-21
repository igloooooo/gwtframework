package com.iglooit.core.command.server.handlers;

import com.clarity.core.base.iface.command.Request;

public interface RequestHandlerCallback
{
    void onStartProcess(Request request, RequestHandler requestHandler, long startTime);
    void onCompleteProcess(Request request, RequestHandler requestHandler, long finishTime, boolean success);
}
