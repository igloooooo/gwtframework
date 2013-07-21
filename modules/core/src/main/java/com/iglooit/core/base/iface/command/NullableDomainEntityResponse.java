package com.iglooit.core.base.iface.command;

import com.clarity.core.base.iface.domain.DomainEntity;

public class NullableDomainEntityResponse<DE extends DomainEntity> extends Response
{
    private DE domainEntity;

    public NullableDomainEntityResponse()
    {
        // gwt serialization only
    }

    public NullableDomainEntityResponse(DE domainEntity)
    {
        setDomainEntity(domainEntity);
    }

    public void setDomainEntity(DE domainEntity)
    {
        this.domainEntity = domainEntity;
    }

    public DE getDomainEntity()
    {
        return domainEntity;
    }
}
