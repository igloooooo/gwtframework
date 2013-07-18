package com.iglooit.commons.iface.domain;

import java.io.Serializable;

public class SearchSorting implements Serializable
{
    private String fieldName;
    private OrderBy orderBy;

    public SearchSorting()
    {
    }

    public SearchSorting(String fieldName, OrderBy orderBy)
    {
        this.fieldName = fieldName;
        this.orderBy = orderBy;
    }

    public String getFieldName()
    {
        return fieldName;
    }

    public void setFieldName(String fieldName)
    {
        this.fieldName = fieldName;
    }

    public OrderBy getOrderBy()
    {
        return orderBy;
    }

    public void setOrderBy(OrderBy orderBy)
    {
        this.orderBy = orderBy;
    }

}
