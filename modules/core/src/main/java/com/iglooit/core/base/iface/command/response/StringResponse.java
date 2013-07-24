package com.iglooit.core.base.iface.command.response;

import com.iglooit.core.base.iface.command.Response;

public class StringResponse extends Response
{
    private String data;

    public StringResponse()
    {

    }

    public StringResponse(String data)
    {
        this.data = data;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
    }
}
