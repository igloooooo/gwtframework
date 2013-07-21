package com.iglooit.core.base.iface.command.request;

import com.clarity.core.base.iface.command.Request;
import com.clarity.core.base.iface.command.Response;

import java.util.List;

public class StringListRequest<ResponseType extends Response> extends Request<ResponseType>
{
    private List<String> stringList;

    public StringListRequest()
    {
    }

    public StringListRequest(List<String> stringList)
    {
        this.stringList = stringList;
    }

    public List<String> getStringList()
    {
        return stringList;
    }

    public void setStringList(List<String> stringList)
    {
        this.stringList = stringList;
    }
}
