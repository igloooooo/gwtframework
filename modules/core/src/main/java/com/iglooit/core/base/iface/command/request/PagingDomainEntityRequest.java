package com.iglooit.core.base.iface.command.request;

import com.clarity.core.base.iface.command.Response;
import com.clarity.commons.iface.domain.SearchPaging;
import com.clarity.core.base.iface.command.ValidatableRequest;
import com.clarity.core.base.iface.domain.DomainEntity;
import com.clarity.commons.iface.domain.SearchSorting;
import com.clarity.core.base.iface.domain.Validatable;
import com.clarity.commons.iface.type.Option;

import java.util.Arrays;
import java.util.List;

public class PagingDomainEntityRequest<DE extends DomainEntity, RT extends Response> extends PagingRequest<RT>
    implements ValidatableRequest
{
    private DE domainEntity;

    public PagingDomainEntityRequest()
    {
        // gwt serialization only
    }

    public PagingDomainEntityRequest(DE domainEntity,
                                     SearchPaging searchPaging,
                                     Option<SearchSorting> searchSortingOpt)
    {
        super(searchPaging, searchSortingOpt);
        this.domainEntity = domainEntity;
    }

    public DE getDomainEntity()
    {
        return domainEntity;
    }

    public void setDomainEntity(DE domainEntity)
    {
        this.domainEntity = domainEntity;
    }

    public List<Validatable> getValidatables()
    {
        return Arrays.asList((Validatable)domainEntity);
    }
}
