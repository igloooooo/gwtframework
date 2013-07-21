package com.iglooit.core.base.iface.command;

import com.clarity.core.base.iface.domain.JpaDomainEntity;
import com.clarity.commons.iface.type.UUID;

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

        final UUID id = getId();
        if (id != null ? !id.equals(that.getId()) : that.getId() != null) return false;

        return true;
    }

    @Override
    public int hashCode()
    {
        final UUID id = getId();
        return id == null ? super.hashCode() : id.hashCode();
    }

    public UUID<DE> getId()
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