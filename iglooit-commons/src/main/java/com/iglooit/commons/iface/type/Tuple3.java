package com.iglooit.commons.iface.type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tuple3<A, B, C> implements Serializable
{
    private A a;
    private B b;
    private C c;

    public Tuple3()
    {

    }

    public Tuple3(A a, B b, C c)
    {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public A getFirst()
    {
        return a;
    }

    public void setFirst(A a)
    {
        this.a = a;
    }

    public void setSecond(B b)
    {
        this.b = b;
    }

    public void setThird(C c)
    {
        this.c = c;
    }

    public B getSecond()
    {
        return b;
    }

    public C getThird()
    {
        return c;
    }

    public static <A, B, C> Tuple3<A, B, C> make(A a, B b, C c)
    {
        return new Tuple3<A, B, C>(a, b, c);
    }

    @Override
    public boolean equals(Object otherObj)
    {
        if (otherObj instanceof Tuple3)
        {
            Tuple3 other = (Tuple3)otherObj;
            return this.getFirst().equals(other.getFirst())
                && this.getSecond().equals(other.getSecond())
                && this.getThird().equals(other.getThird());
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return a.hashCode() * 7 + b.hashCode() * 31 + c.hashCode() * 79;
    }

    public static <A, B, C> List<A> getFirsts(List<Tuple3<A, B, C>> tupleList)
    {
        List<A> ret = new ArrayList<A>();
        if (tupleList == null)
            return ret;
        for (Tuple3<A, B, C> tuple : tupleList)
            ret.add(tuple.getFirst());
        return ret;
    }

    public static <A, B, C> List<B> getSeconds(List<Tuple3<A, B, C>> tupleList)
    {
        List<B> ret = new ArrayList<B>();
        if (tupleList == null)
            return ret;
        for (Tuple3<A, B, C> tuple : tupleList)
            ret.add(tuple.getSecond());
        return ret;
    }

    public static <A, B, C> List<C> getThirds(List<Tuple3<A, B, C>> tupleList)
    {
        List<C> ret = new ArrayList<C>();
        if (tupleList == null)
            return ret;
        for (Tuple3<A, B, C> tuple : tupleList)
            ret.add(tuple.getThird());
        return ret;
    }
}
