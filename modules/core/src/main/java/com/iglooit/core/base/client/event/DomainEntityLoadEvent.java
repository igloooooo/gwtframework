package com.iglooit.core.base.client.event;

import com.clarity.core.base.iface.domain.DomainEntity;

public abstract class DomainEntityLoadEvent<DE extends DomainEntity> extends BasicPayloadEvent<DE>
{
    public DomainEntityLoadEvent(DE domainEntity)
    {
        super(domainEntity);
    }

    public DE getDomainEntity()
    {
        return getPayload();
    }

    public void setDomainEntity(DE domainEntity)
    {
        setPayload(domainEntity);
    }
}
