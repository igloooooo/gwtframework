package com.iglooit.core.lib.server.spring;

import com.clarity.commons.iface.type.AppX;

import java.util.*;

/**
 * Overrides all the methods of HashMap (the super class of Properties) and delegates to a @see LinkMap instead.
 */
public class OrderedProperties extends Properties
{

    private static final long serialVersionUID = 1L;

    private Map<Object, Object> linkMap = new LinkedHashMap<Object, Object>();

    @Override
    public synchronized Object put(Object key, Object value)
    {
        return linkMap.put(key, value);
    }

    @Override
    public synchronized boolean contains(Object value)
    {
        return linkMap.containsValue(value);
    }

    @Override
    public boolean containsValue(Object value)
    {
        return linkMap.containsValue(value);
    }

    @Override
    public synchronized Enumeration<Object> elements()
    {
        return Collections.enumeration(linkMap.values());
    }

    @Override
    public Set<Map.Entry<Object, Object>> entrySet()
    {
        return linkMap.entrySet();
    }

    @Override
    public synchronized Enumeration<Object> keys()
    {
        return Collections.enumeration(linkMap.keySet());
    }

    @Override
    public synchronized void clear()
    {
        linkMap.clear();
    }

    @Override
    public synchronized boolean containsKey(Object key)
    {
        return linkMap.containsKey(key);
    }

    @Override
    public Collection<Object> values()
    {
        return linkMap.values();
    }

    @Override
    public synchronized int size()
    {
        return linkMap.size();
    }

    @Override
    public synchronized boolean isEmpty()
    {
        return linkMap.isEmpty();
    }

    @Override
    public synchronized Object get(Object key)
    {
        return linkMap.get(key);
    }

    @Override
    protected void rehash()
    {
        throw new UnsupportedOperationException("Not implemented on a linked hashmap");
    }

    @Override
    public synchronized Object remove(Object key)
    {
        return linkMap.remove(key);
    }

    @Override
    public synchronized void putAll(Map<? extends Object, ? extends Object> t)
    {
        linkMap.putAll(t);
    }

    public synchronized Object clone()
    {
        throw new AppX("Ordered properties does not support cloning");
    }

    @Override
    public synchronized String toString()
    {
        return linkMap.toString();
    }

    @Override
    public Set<Object> keySet()
    {
        return linkMap.keySet();
    }

    @Override
    public synchronized boolean equals(Object o)
    {
        return linkMap.equals(o);
    }

    @Override
    public synchronized int hashCode()
    {
        return linkMap.hashCode();
    }
}
