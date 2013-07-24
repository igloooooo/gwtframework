package com.iglooit.core.base.iface.command.response;

import com.iglooit.core.base.iface.command.Response;

public class StringArrayResponse extends Response
{
    private String[] data;

    public StringArrayResponse()
    {
    }

    public StringArrayResponse(String[] data)
    {
        this.data = data;
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
