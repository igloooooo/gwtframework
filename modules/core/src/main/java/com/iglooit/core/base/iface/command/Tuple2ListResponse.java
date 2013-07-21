package com.iglooit.core.base.iface.command;

import com.clarity.commons.iface.type.Tuple2;

import java.util.List;

public class Tuple2ListResponse<A, B> extends Response
{
    private List<Tuple2<A, B>> values;

    public Tuple2ListResponse()
    {

    }

    public Tuple2ListResponse(List<Tuple2<A, B>> values)
    {
        this.values = values;
    }

    public List<Tuple2<A, B>> getValues()
    {
        return values;
    }

    public void setValues(List<Tuple2<A, B>> values)
    {
        this.values = values;
    }
}
