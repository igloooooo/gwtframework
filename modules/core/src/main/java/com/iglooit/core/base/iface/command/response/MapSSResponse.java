package com.iglooit.core.base.iface.command.response;

import com.clarity.core.base.iface.command.Response;

import java.util.Map;

public class MapSSResponse extends Response
{
    private Map<String, String> map;

    public MapSSResponse()
    {

    }

    public MapSSResponse(Map<String, String> map)
    {
        this.map = map;
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
