package com.iglooit.core.base.iface.command.request;

import com.clarity.core.base.iface.command.Request;
import com.clarity.core.base.iface.command.Response;
import com.clarity.commons.iface.type.UUID;

public class UUIDRequest<T, ResponseType extends Response>
    extends Request<ResponseType>
{
    private UUID<T> id;

    public UUIDRequest()
    {

    }

    public UUIDRequest(UUID<T> id)
    {
        this.id = id;
    }


    public UUID<T> getId()
    {
        return id;
    }

    public void setId(UUID<T> id)
    {
        this.id = id;
    }
}
