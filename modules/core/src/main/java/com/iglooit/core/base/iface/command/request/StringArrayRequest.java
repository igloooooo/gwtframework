package com.iglooit.core.base.iface.command.request;

import com.iglooit.core.base.iface.command.Request;
import com.iglooit.core.base.iface.command.Response;

public abstract class StringArrayRequest<ResponseType extends Response> extends Request<ResponseType>
{

    private String[] data;

    public StringArrayRequest()
    {
    }

    protected StringArrayRequest(String[] data)
    {
        this.data = data;
    }

    protected StringArrayRequest(String data)
    {
        this.data = new String[]{data};
    }

    public String[] getData()
    {
        return data;
    }

    public void setData(String[] data)
    {
        this.data = data;
    }
}
