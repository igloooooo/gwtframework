package com.iglooit.core.lib.iface.filters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class Filter<T>
{
    public List<T> filter(Collection<T> collection)
    {
        List<T> filtered = new ArrayList<T>();
        for (T o : collection)
            if (satisfies(o))
                filtered.add(o);
        return filtered;
    }

    public abstract boolean satisfies(T object);
}
