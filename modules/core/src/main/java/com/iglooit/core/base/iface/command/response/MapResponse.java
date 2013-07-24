package com.iglooit.core.base.iface.command.response;

import com.iglooit.core.base.iface.command.Response;

import java.util.Map;

public class MapResponse<T, K> extends Response
{
    private Map<T, K> map;

    public MapResponse()
    {

    }

    public MapResponse(Map<T, K> map)
    {
        this.map = map;
    }

    public Map<T, K> getMap()
    {
        return map;
    }

    public void setMap(Map<T, K> map)
    {
        this.map = map;
    }
}
