package com.iglooit.coreum.account.iface.domain;

import com.iglooit.coreum.base.iface.domain.JpaDomainEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "anonymous_user_roles")
public class AnonymousUserRole extends JpaDomainEntity<AnonymousUserRole>
{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anur_orga_id")
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "anur_userrole_id")
    private UserRole userRole;

    public AnonymousUserRole(Organization organization, UserRole userRole)
    {
        this.organization = organization;
        this.userRole = userRole;
    }

    public AnonymousUserRole()
    {
    }

    public Organization getOrganization()
    {
        return organization;
    }

    public void setOrganization(Organization organization)
    {
        this.organization = organization;
    }

    public UserRole getUserRole()
    {
        return userRole;
    }

    public void setUserRole(UserRole userRole)
    {
        this.userRole = userRole;
    }
}
