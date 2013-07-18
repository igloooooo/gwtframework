package com.iglooit.commons.iface.type;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Tuple2<A, B> implements Serializable
{
    private A a;
    private B b;

    public Tuple2()
    {

    }

    public Tuple2(A a, B b)
    {
        this.a = a;
        this.b = b;
    }

    public A  getFirst()
    {
        return a;
    }

    public B getSecond()
    {
        return b;
    }

    public static <A, B> Tuple2<A, B> make(A a, B b)
    {
        return new Tuple2<A, B>(a, b);
    }

    @Override
    public boolean equals(Object otherObj)
    {
        if (otherObj instanceof Tuple2)
        {
            Tuple2 other = (Tuple2)otherObj;
            return this.getFirst().equals(other.getFirst())
                && this.getSecond().equals(other.getSecond());
        }
        return false;
    }

    @Override
    public int hashCode()
    {
        return a.hashCode() * 7 + b.hashCode() * 31;
    }

    public static <A, B> List<A> getFirsts(List<Tuple2<A, B>> tupleList)
    {
        List<A> ret = new ArrayList<A>();

        if (tupleList == null)
            return ret;

        for (Tuple2<A, B> tuple : tupleList)
            ret.add(tuple != null ? tuple.getFirst() : null);
        return ret;
    }

    public static <A, B> List<B> getSeconds(List<Tuple2<A, B>> tupleList)
    {
        List<B> ret = new ArrayList<B>();

        if (tupleList == null)
            return ret;

        for (Tuple2<A, B> tuple : tupleList)
            ret.add(tuple != null ? tuple.getSecond() : null);
        return ret;
    }

    public static <A, B> Map<A, B> toMap(List<Tuple2<A, B>> tupleList)
    {
        Map<A, B> ret = new HashMap<A, B>();

        if (tupleList == null)
            return ret;

        for (Tuple2<A, B> tuple : tupleList)
            ret.put(
                tuple != null ? tuple.getFirst() : null,
                tuple != null ? tuple.getSecond() : null);
        return ret;
    }


    public static <A, B> List<Tuple2<A, B>> fromMap(Map<A, B> values)
    {
        List<Tuple2<A, B>> converted = new ArrayList<Tuple2<A, B>>();

        for (Map.Entry<A, B> entry : values.entrySet())
        {
            converted.add(new Tuple2<A, B>(entry.getKey(), entry.getValue()));
        }

        return converted;
    }

    public static <A, B> boolean contains(List<Tuple2<A, B>> tupleList, Tuple2<A, B> tuple)
    {
        if (tupleList == null)
            return false;
        for (Tuple2<A, B> t : tupleList)
            if (t != null && t.equals(tuple))
                return true;
        return false;
    }

    public void setFirst(A a)
    {
        this.a = a;
    }

    public void setSecond(B b)
    {
        this.b = b;
    }

    @Override
    public String toString()
    {
        return a.toString() + "\t->\t" + b.toString();
    }
}
