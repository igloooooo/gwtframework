package com.iglooit.core.base.iface.command.request;

import com.iglooit.commons.iface.type.UUID;
import com.iglooit.core.base.iface.command.Request;
import com.iglooit.core.base.iface.command.Response;

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
