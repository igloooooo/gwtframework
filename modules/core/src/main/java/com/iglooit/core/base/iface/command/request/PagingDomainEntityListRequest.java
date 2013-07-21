package com.iglooit.core.base.iface.command.request;

import com.clarity.core.base.iface.command.Response;
import com.clarity.commons.iface.domain.SearchPaging;
import com.clarity.core.base.iface.command.ValidatableRequest;
import com.clarity.core.base.iface.domain.DomainEntity;
import com.clarity.commons.iface.domain.SearchSorting;
import com.clarity.core.base.iface.domain.Validatable;
import com.clarity.commons.iface.type.Option;

import java.util.ArrayList;
import java.util.List;

public class PagingDomainEntityListRequest<DE extends DomainEntity, RT extends Response>
    extends PagingRequest<RT>
    implements ValidatableRequest
{
    private List<DE> domainEntityList;

    public PagingDomainEntityListRequest()
    {
        // gwt serialization only
    }

    public PagingDomainEntityListRequest(SearchPaging searchPaging,
                                         Option<SearchSorting> searchSortingOpt,
                                         List<DE> domainEntityList)
    {
        super(searchPaging, searchSortingOpt);
        this.domainEntityList = domainEntityList;
    }

    public List<DE> getDomainEntityList()
    {
        return domainEntityList;
    }

    public void setDomainEntityList(List<DE> domainEntityList)
    {
        this.domainEntityList = domainEntityList;
    }

    public List<Validatable> getValidatables()
    {
        return new ArrayList<Validatable>(domainEntityList);
    }
}
