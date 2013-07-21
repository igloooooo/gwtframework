package com.iglooit.core.base.iface.command.request;

import com.clarity.core.base.iface.command.Request;
import com.clarity.core.base.iface.command.Response;

public class StringRequest<ResponseType extends Response>
    extends Request<ResponseType>
{
    private String string;

    public StringRequest()
    {

    }

    public StringRequest(String string)
    {
        this.string = string;
    }

    public String getString()
    {
        return string;
    }

    public void setString(String s1)
    {
        this.string = s1;
    }
}
