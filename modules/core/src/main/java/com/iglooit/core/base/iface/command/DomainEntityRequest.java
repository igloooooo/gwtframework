package com.iglooit.core.base.iface.command;

import com.clarity.core.base.iface.domain.JpaDomainEntity;
import com.clarity.core.base.iface.domain.Validatable;

import java.util.Arrays;
import java.util.List;

public abstract class
    DomainEntityRequest<DE extends JpaDomainEntity, ResponseType extends Response>
    extends Request<ResponseType> implements ValidatableRequest
{
    private DE domainEntity;

    public DomainEntityRequest()
    {
        // gwt serialization only
    }

    public DomainEntityRequest(DE domainEntity)
    {
        setDomainEntity(domainEntity);
    }

    public DE getDomainEntity()
    {
        // add stuff to re-enhance a DE from the client
        // once we're back in the server.

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
