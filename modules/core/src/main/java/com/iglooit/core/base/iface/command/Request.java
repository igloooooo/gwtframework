package com.iglooit.core.base.iface.command;

import java.io.Serializable;

// ResponseType is unused, but is kept here to add type safety when creating request handlers.
public abstract class Request<ResponseType extends Response> implements Serializable
{
    public Request()
    {
    }


}
