package com.iglooit.core.base.iface.command.response;

import com.iglooit.commons.iface.type.Tuple3;
import com.iglooit.core.base.iface.command.Response;

public class Tuple3Response<A, B, C> extends Response
{
    private Tuple3<A, B, C> value;

    public Tuple3Response()
    {
    }

    public Tuple3Response(Tuple3<A, B, C> value)
    {
        this.value = value;
    }

    public Tuple3Response(A a, B b, C c)
    {
        this.value = new Tuple3<A, B, C>(a, b, c);
    }


    public Tuple3<A, B, C> getValue()
    {
        return value;
    }

    public void setValue(Tuple3<A, B, C> value)
    {
        this.value = value;
    }
}
