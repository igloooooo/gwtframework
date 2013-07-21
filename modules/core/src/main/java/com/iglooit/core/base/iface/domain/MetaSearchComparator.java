package com.iglooit.core.base.iface.domain;

import com.clarity.commons.iface.domain.meta.Meta;
import com.clarity.commons.iface.domain.SearchSorting;
import com.clarity.commons.iface.type.Option;

import java.util.Comparator;

public class MetaSearchComparator<T extends Meta> implements Comparator<T>
{
    private final Comparator<T> comparatorFunc;

    @Override
    public int compare(T o1, T o2)
    {
        return comparatorFunc.compare(o1, o2);
    }

    public MetaSearchComparator(final Option<SearchSorting> searchSortingOption,
                                final String defaultCompareProperty)
    {
        if (searchSortingOption.isNone())
        {
            final int comparatorDirection = 1;
            comparatorFunc = new Comparator<T>()
            {
                public int compare(T o1, T o2)
                {
                    Comparable c1 = o1.get(defaultCompareProperty);
                    Comparable c2 = o2.get(defaultCompareProperty);
                    return c1.compareTo(c2) * comparatorDirection;
                }
            };
        }
        else
        {
            final int comparatorDirection = searchSortingOption.value().getOrderBy().getComparatorInt();
            final String propertyName = searchSortingOption.value().getFieldName();
            comparatorFunc = new Comparator<T>()
            {
                public int compare(T o1, T o2)
                {
                    Comparable c1 = o1.get(propertyName);
                    Comparable c2 = o2.get(propertyName);
                    return c1.compareTo(c2) * comparatorDirection;
                }
            };
        }
    }
}
