package com.iglooit.core.base.iface.command.response;

import com.clarity.core.base.iface.command.Response;

public class IntResponse extends Response
{
    private int data;

    public IntResponse()
    {

    }

    public IntResponse(int data)
    {
        this.data = data;
    }

    public int getData()
    {
        return data;
    }

    public void setData(int data)
    {
        this.data = data;
    }
}
