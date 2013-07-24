package com.iglooit.core.base.iface.command;

import com.iglooit.commons.iface.type.UUID;
import com.iglooit.core.base.iface.domain.JpaDomainEntity;

import java.io.Serializable;

public abstract class ComposedDomainEntity<DE extends JpaDomainEntity<DE>> implements Serializable
{
    private DE domainEntity;

    public ComposedDomainEntity()
    {
    }

    protected ComposedDomainEntity(DE domainEntity)
    {
        this.domainEntity = domainEntity;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComposedDomainEntity that = (ComposedDomainEntity)o;

        final Long id = getId();
        if (id != null ? !id.equals(that.getId()) : that.getId() != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        final Long id = getId();
        return id == null ? super.hashCode() : id.hashCode();
    }

    public Long getId()
    {
        return domainEntity.getId();
    }

    public DE getDomainEntity()
    {
        return domainEntity;
    }

    public void setDomainEntity(DE domainEntity)
    {
        this.domainEntity = domainEntity;
    }
}