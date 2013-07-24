package com.iglooit.coreum.base.iface.domain;

import com.iglooit.commons.iface.type.UUID;
import com.iglooit.coreum.lib.server.hibernate.UUIDUserType;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Version;

@MappedSuperclass
public abstract class JpaDomainEntity<InheritingClass extends JpaDomainEntity>
{
    @Id
    @Column(name="id")
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Version
    @Column(nullable = false)
    private int lockVersion = 0;

    private static transient UUIDFactory uuidFactory;

    public JpaDomainEntity()
    {
        //UUID factory should be populated by the entry point for all modules.
        //ID should not be null, however it may be in scenario's such as test cases,
        //and using the IntelliJ Persistence framework,
        //in these cases, we do not want to throw an appX but it is acceptable to return a null ID.
//        this.id = uuidFactory == null ? null : uuidFactory.<InheritingClass>generate();
    }

    protected JpaDomainEntity(Integer id)
    {
        this.id = id;
    }

    @Override
    public int hashCode()
    {
        return getId() == null ? 1 : getId().hashCode();
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

    public Integer getId()
    {
        return id;
    }
}
