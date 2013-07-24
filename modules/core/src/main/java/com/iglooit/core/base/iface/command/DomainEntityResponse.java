package com.iglooit.core.base.iface.command;

import com.iglooit.commons.iface.type.AppX;
import com.iglooit.core.base.iface.domain.DomainEntity;

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
