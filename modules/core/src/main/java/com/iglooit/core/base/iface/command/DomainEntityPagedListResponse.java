package com.iglooit.core.base.iface.command;

import com.clarity.commons.iface.domain.SearchPaging;
import com.clarity.core.base.iface.domain.DomainEntity;

import java.util.List;

public class DomainEntityPagedListResponse<T extends DomainEntity> extends DomainEntityListResponse<T>
{
    private SearchPaging searchPaging;

    public DomainEntityPagedListResponse()
    {
    }

    public DomainEntityPagedListResponse(List<T> domainEntityList, SearchPaging searchPaging)
    {
        super(domainEntityList);
        this.searchPaging = searchPaging;
    }

    public SearchPaging getSearchPaging()
    {
        return searchPaging;
    }

    public void setSearchPaging(SearchPaging searchPaging)
    {
        this.searchPaging = searchPaging;
    }
}
