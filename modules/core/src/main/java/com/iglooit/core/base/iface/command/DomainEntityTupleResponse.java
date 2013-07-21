package com.iglooit.core.base.iface.command;

import com.clarity.core.base.iface.domain.DomainEntity;

public class DomainEntityTupleResponse <A extends DomainEntity, B extends DomainEntity>
    extends Response
{
    private A first;
    private B second;

    public DomainEntityTupleResponse()
    {
        
    }

    public DomainEntityTupleResponse(A first, B second)
    {
        this.first = first;
        this.second = second;
    }

    public A getFirst()
    {
        return first;
    }

    public void setFirst(A first)
    {
        this.first = first;
    }

    public B getSecond()
    {
        return second;
    }

    public void setSecond(B second)
    {
        this.second = second;
    }
}
