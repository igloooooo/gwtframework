package com.iglooit.core.base.iface.command.request;

import com.iglooit.core.base.iface.command.Request;
import com.iglooit.core.base.iface.command.Response;

import java.util.Map;

public class StringMapSSRequest<ResponseType extends Response>
    extends Request<ResponseType>
{
    private String string;
    private Map<String, String> map;

    public StringMapSSRequest()
    {
    }

    public StringMapSSRequest(String string, Map<String, String> map)
    {
        this.string = string;
        this.map = map;
    }

    public String getString()
    {
        return string;
    }

    public void setString(String string)
    {
        this.string = string;
    }

    public Map<String, String> getMap()
    {
        return map;
    }

    public void setMap(Map<String, String> map)
    {
        this.map = map;
    }
}
