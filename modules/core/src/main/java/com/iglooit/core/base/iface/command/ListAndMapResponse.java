package com.iglooit.core.base.iface.command;

import com.clarity.commons.iface.type.AppX;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListAndMapResponse<ListType, MapValueType> extends Response
{
    private List<ListType> list;
    private Map<String, List<MapValueType>> map;

    public ListAndMapResponse()
    {
        list = new ArrayList<ListType>();
        map = new HashMap<String, List<MapValueType>>();
    }

    public ListAndMapResponse(List<ListType> list, Map<String, List<MapValueType>> map)
    {
        this.list = list;
        this.map = map;
    }

    public List<ListType> getList()
    {
        if (list == null)
            throw new AppX("Bad implementation ListResponse has no list set.");
        return list;
    }

    public void setList(List<ListType> list)
    {
        this.list = list;
    }

    public Map<String, List<MapValueType>> getMap()
    {
        if (map == null)
            throw new AppX("Bad implementation ListResponse has no map set.");
        return map;
    }

    public void setMap(Map<String, List<MapValueType>> map)
    {
        this.map = map;
    }
}
