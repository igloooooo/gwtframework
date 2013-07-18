package com.iglooit.commons.iface.domain;

public enum OrderBy
{
    ASCENDING(1, "asc"), DESCENDING(-1, "desc");

    private final Integer comparatorInt;
    private final String orderString;

    private OrderBy(Integer comparatorInt, String orderString)
    {
        this.comparatorInt = comparatorInt;
        this.orderString = orderString;
    }

    public int getComparatorInt()
    {
        return comparatorInt;
    }

    public String toString()
    {
        return orderString;
    }
}
