package com.iglooit.core.base.iface.command;

import com.clarity.core.base.iface.domain.DomainEntity;
import com.clarity.commons.iface.type.AppX;

public class DomainEntityResponse<DE extends DomainEntity> extends Response
{
    private DE domainEntity;

    public DomainEntityResponse()
    {
        // gwt serialization only
    }

    public DomainEntityResponse(DE domainEntity)
    {
        setDomainEntity(domainEntity);
    }

    public DE getDomainEntity()
    {
        if (domainEntity == null)
            throw new AppX("Bad implementation DomainEntityResponse has no DE set.");
        return domainEntity;
    }

    public void setDomainEntity(DE domainEntity)
    {
        this.domainEntity = domainEntity;
    }
}
