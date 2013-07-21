package com.iglooit.core.lib.iface;

import java.util.*;

public class ListUtil
{
    public static <From, To> List<To> convert(List<From> items, Converter<From, To> converter)
    {
        List<To> results = new ArrayList<To>();
        for (From item : items)
            results.add(converter.convertItem(item));
        return results;
    }

    public interface Converter<From, To>
    {
        To convertItem(From from);
    }

    public static <T> List<T> getDistinctList(List<T> stringList)
    {
        Set<T> distinctSet = new HashSet<T>(stringList);
        return new ArrayList<T>(distinctSet);
    }

    public static <T> List<T> getIntersectList(List<T> listA,
                                               List<T> listB)
    {
        Set<T> setA = new HashSet<T>(listA);
        Set<T> setB = new HashSet<T>(listB);
        List<T> result = new ArrayList<T>(listB.size());

        for (T b : setB)
        {
            if (setA.contains(b))
                result.add(b);
        }
        return result;
    }

    public static <T> List<T> getAOuterB(Collection<T> listA, Collection<T> listB)
    {
        Set<T> setA = new HashSet<T>(listA);
        Set<T> setB = new HashSet<T>(listB);
        List<T> result = new ArrayList<T>();

        for (T a : setA)
        {
            if (!setB.contains(a))
                result.add(a);
        }
        return result;
    }
}
