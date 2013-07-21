package com.iglooit.core.base.iface.command.request;

import com.clarity.core.base.iface.command.Request;
import com.clarity.core.base.iface.command.Response;
import com.clarity.commons.iface.domain.SearchPaging;
import com.clarity.commons.iface.domain.OrderBy;
import com.clarity.commons.iface.domain.SearchSorting;
import com.clarity.commons.iface.type.Option;

public abstract class PagingRequest<ResponseType extends Response> extends Request<ResponseType>
{
    private SearchPaging searchPaging;
    private Option<SearchSorting> searchSortingOpt;

    public PagingRequest()
    {
    }

    public PagingRequest(SearchPaging searchPaging, Option<SearchSorting> searchSortingOpt)
    {
        this.searchPaging = searchPaging;
        this.searchSortingOpt = searchSortingOpt;
    }

    public SearchPaging getSearchPaging()
    {
        return searchPaging;
    }

    public void setSearchPaging(SearchPaging searchPaging)
    {
        this.searchPaging = searchPaging;
    }

    public Option<SearchSorting> getSearchSortingOpt()
    {
        return searchSortingOpt;
    }

    public String getSearchSortingOptFieldName()
    {
        return getSearchSortingOpt().isSome() ? getSearchSortingOpt().value().getFieldName() : "";
    }

    public void setSearchSortingOpt(Option<SearchSorting> searchSortingOpt)
    {
        this.searchSortingOpt = searchSortingOpt;
    }

    public OrderBy getSearchSortingOrderBy()
    {
        return getSearchSortingOpt().isSome() ? getSearchSortingOpt().value().getOrderBy() : OrderBy.ASCENDING;
    }

    public int getSearchSortingComparatorInt()
    {
        return getSearchSortingOpt().isSome() ? getSearchSortingOpt().value().getOrderBy().getComparatorInt() : 1;
    }

    public String getSearchSortingComparatorString()
    {
        return getSearchSortingComparatorInt() == 1 ? OrderBy.ASCENDING.toString() : OrderBy.DESCENDING.toString();
    }
}
