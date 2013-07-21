package com.iglooit.core.base.iface.command.request;

import com.clarity.core.base.iface.command.Request;
import com.clarity.core.base.iface.command.Response;

public class StringStringRequest<ResponseType extends Response>
    extends Request<ResponseType>
{
    private String s1;
    private String s2;

    public StringStringRequest()
    {

    }

    public StringStringRequest(String s1, String s2)
    {
        this.s1 = s1;
        this.s2 = s2;
    }

    public String getS1()
    {
        return s1;
    }

    public void setS1(String s1)
    {
        this.s1 = s1;
    }

    public String getS2()
    {
        return s2;
    }

    public void setS2(String s2)
    {
        this.s2 = s2;
    }
}