package com.iglooit.coreum.account.iface.domain;

import com.iglooit.commons.iface.type.UUID;
import com.iglooit.coreum.base.iface.domain.NamedJpaDomainEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PARTY_ROLE_TYPES")
public class PartyRoleType extends NamedJpaDomainEntity<PartyRoleType>
{
    public static final String EMPLOYEE = "EMPLOYEE";
    public static final String EMPLOYER = "EMPLOYER";
    public static final String USER = "USER";

    public PartyRoleType()
    {

    }

    public PartyRoleType(String name, String desc)
    {
        super(name, desc);
    }

    public PartyRoleType(UUID id, String name, String desc)
    {
        super(id, name, desc);
    }
}
