package com.iglooit.core.base.iface.command.response;

import com.iglooit.core.base.iface.command.Response;

public class DataResponse<T> extends Response
{
    private T data;

    public DataResponse()
    {

    }

    public DataResponse(T data)
    {
        this.data = data;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }
}
