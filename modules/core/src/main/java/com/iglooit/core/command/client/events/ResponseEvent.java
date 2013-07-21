package com.iglooit.core.command.client.events;

import com.clarity.core.base.iface.command.Response;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

// todo ms: this might be a really bad idea, making a whole system
// out of plugs and converters. but it might help too, will see

/**
 * class used for forwarding the rpc response through mvp towards the view
 *
 * @param <ResponseType>
 */
public abstract class ResponseEvent<ResponseType extends Response, EventHandlerType extends EventHandler>
    extends GwtEvent<EventHandlerType>
{
    private final ResponseType response;

    public ResponseEvent(ResponseType response)
    {
        this.response = response;
    }

    public ResponseType getResponse()
    {
        return response;
    }
}
