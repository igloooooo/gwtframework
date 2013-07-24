package com.iglooit.core.base.iface.domain;

import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public abstract class JpaDomainEntity<InheritingClass extends JpaDomainEntity> extends DomainEntity
{
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Version
    @Column(nullable = false)
    private int lockVersion = 0;

    private static transient UUIDFactory uuidFactory;

    public JpaDomainEntity()
    {
        this.id = null;
    }

    protected JpaDomainEntity(Long id)
    {
        this.id = id;
    }

    @Override
    public int hashCode()
    {
        return getId() == null ? 1 : getId().intValue();
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this.id == null)
            return false;
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof JpaDomainEntity))
            return false;

        final JpaDomainEntity other = (JpaDomainEntity)obj;
        return getId().equals(other.getId());
    }

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public int getLockVersion()
    {
        return lockVersion;
    }

    public void setLockVersion(int lockVersion)
    {
        this.lockVersion = lockVersion;
    }

    public static UUIDFactory getUuidFactory()
    {
        return uuidFactory;
    }

    public static void setUuidFactory(UUIDFactory uuidFactory)
    {
        JpaDomainEntity.uuidFactory = uuidFactory;
    }
}
