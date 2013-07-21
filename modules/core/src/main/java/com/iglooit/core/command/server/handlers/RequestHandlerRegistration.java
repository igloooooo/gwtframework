package com.iglooit.core.command.server.handlers;

public class RequestHandlerRegistration
{
    private final RequestHandler handler;
    private final RequestHandlerManager manager;

    public RequestHandlerRegistration(RequestHandler handler, RequestHandlerManager manager)
    {
        this.handler = handler;
        this.manager = manager;
    }

    public void remove()
    {
        manager.removeRequestHandler(handler);
    }
}
