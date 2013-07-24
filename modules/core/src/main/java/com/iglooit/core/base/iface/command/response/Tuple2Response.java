package com.iglooit.core.base.iface.command.response;

import com.iglooit.commons.iface.type.Tuple2;
import com.iglooit.core.base.iface.command.Response;

public class Tuple2Response<A, B> extends Response
{
    private Tuple2<A, B> value;

    public Tuple2Response()
    {
    }

    public Tuple2Response(Tuple2<A, B> value)
    {
        this.value = value;
    }

    public Tuple2Response(A a, B b)
    {
        this.value = new Tuple2<A, B>(a, b);
    }


    public Tuple2<A, B> getValue()
    {
        return value;
    }

    public void setValue(Tuple2<A, B> value)
    {
        this.value = value;
    }
}
