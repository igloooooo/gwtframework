package com.iglooit.core.base.iface.command;

import com.iglooit.commons.iface.domain.SearchPaging;

import java.util.List;

public class PagedListResponse<T> extends ListResponse<T>
{
    private SearchPaging searchPaging;

    public PagedListResponse()
    {
    }

    public PagedListResponse(List<T> ts, SearchPaging searchPaging)
    {
        super(ts);
        this.searchPaging = searchPaging;
    }

    public SearchPaging getSearchPaging()
    {
        return searchPaging;
    }

    protected void setSearchPaging(SearchPaging searchPaging)
    {
        this.searchPaging = searchPaging;
    }
}
