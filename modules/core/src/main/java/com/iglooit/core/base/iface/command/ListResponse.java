package com.iglooit.core.base.iface.command;

import com.clarity.commons.iface.type.AppX;

import java.util.ArrayList;
import java.util.List;

public class ListResponse<T> extends Response
{
    private List<T> list;

    public ListResponse()
    {
        list = new ArrayList<T>();
    }

    public ListResponse(List<T> list)
    {
        this.list = list;
    }

    public List<T> getList()
    {
        if (list == null)
            throw new AppX("Bad implementation ListResponse has no list set.");
        return list;
    }

    public void setList(List<T> list)
    {
        this.list = list;
    }

    protected void add(T t)
    {
        list.add(t);
    }

}
