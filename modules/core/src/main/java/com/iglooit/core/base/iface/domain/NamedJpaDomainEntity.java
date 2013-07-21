package com.iglooit.core.base.iface.domain;

import com.clarity.commons.iface.util.StringUtil;
import com.clarity.commons.iface.type.UUID;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class NamedJpaDomainEntity<InheritingClass extends JpaDomainEntity>
    extends JpaDomainEntity<InheritingClass> 
{
    private String name;
    private String description;

    public NamedJpaDomainEntity()
    {
    }

    protected NamedJpaDomainEntity(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    protected NamedJpaDomainEntity(UUID id, String name, String description)
    {
        super(id);
        this.name = name;
        this.description = description;
    }

    public String getName()
    {
        return StringUtil.emptyStringIfNull(name);
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDescription()
    {
        return StringUtil.emptyStringIfNull(description);
    }

    public void setDescription(String description)
    {
        this.description = description;
    }
}
