package com.iglooit.core.lib.iface;

import java.util.LinkedList;

public class AtomicQueue<T>
{
    private LinkedList<T> list = new LinkedList<T>();

    public synchronized void push(T item)
    {
        list.add(item);
    }

    public synchronized T poll()
    {
        return list.poll();
    }

    public synchronized boolean isEmpty()
    {
        return list.isEmpty();
    }

    public synchronized int size()
    {
        return list.size();

    }
}
