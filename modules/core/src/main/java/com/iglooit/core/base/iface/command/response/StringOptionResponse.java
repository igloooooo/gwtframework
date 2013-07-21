package com.iglooit.core.base.iface.command.response;

import com.clarity.core.base.iface.command.Response;
import com.clarity.commons.iface.type.Option;

public class StringOptionResponse extends Response
{
    private String data;

    public Option<String> getOption()
    {
        if (data == null)
            return Option.none();
        return Option.some(data);
    }

    public StringOptionResponse()
    {

    }

    public StringOptionResponse(String data)
    {
        this.data = data;
    }

    protected String getData()
    {
        return data;
    }

    protected void setData(String data)
    {
        this.data = data;
    }

}
