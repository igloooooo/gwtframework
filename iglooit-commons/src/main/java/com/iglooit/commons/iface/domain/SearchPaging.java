package com.iglooit.commons.iface.domain;

import java.io.Serializable;

public class SearchPaging implements Serializable
{
    private int pageSize;
    private int firstHitIndex;
    private boolean totalHitsFound = false;
    private long totalHits;
    private String query;

    public String getQuery()
    {
        return query;
    }

    public void setQuery(String query)
    {
        this.query = query;
    }

    public SearchPaging()
    {
    }

    public SearchPaging(int pageSize)
    {
        this.pageSize = pageSize;
        this.totalHits = -1;
    }

    public SearchPaging(int pageSize, int firstHitIndex)
    {
        this.pageSize = pageSize;
        this.firstHitIndex = firstHitIndex;
    }

    public SearchPaging(int pageSize, int firstHitIndex, long totalHits, String query)
    {
        this.pageSize = pageSize;
        this.firstHitIndex = firstHitIndex;
        this.totalHits = totalHits;
        this.query = query;
        totalHitsFound = true;
    }

    public SearchPaging(int pageSize, int firstHitIndex, long totalHits)
    {
        this.pageSize = pageSize;
        this.firstHitIndex = firstHitIndex;
        this.totalHits = totalHits;
        totalHitsFound = true;
    }

    public int getPageSize()
    {
        return pageSize;
    }

    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }

    public long getTotalHits()
    {
        return totalHits;
    }

    public void setTotalHits(long totalHits)
    {
        this.totalHits = totalHits;
    }

    public int getFirstHitIndex()
    {
        return firstHitIndex;
    }

    public void setFirstHitIndex(int firstHitIndex)
    {
        this.firstHitIndex = firstHitIndex;
    }

    public boolean isTotalHitsFound()
    {
        return totalHitsFound;
    }

    public void setTotalHitsFound(boolean totalHitsFound)
    {
        this.totalHitsFound = totalHitsFound;
    }

    @Override
    public String toString()
    {
        return "SearchPaging{" +
            "pageSize=" + pageSize +
            ", firstHitIndex=" + firstHitIndex +
            ", totalHits=" + totalHits +
            '}';
    }
}
