package com.iglooit.coreum.account.iface.domain;

import com.iglooit.coreum.base.iface.domain.JpaDomainEntity;
import com.iglooit.coreum.security.iface.access.domain.Privilege;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "USER_ORG_SECURITY_ROLES")
public class UserOrgSecurityRole extends JpaDomainEntity<UserOrgSecurityRole>
{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USOR_USERROLEID", nullable = false)
    private UserRole userRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USOR_ORGSECURITYROLEID", nullable = false)
    private OrgSecurityRole orgSecurityRole;

    public UserOrgSecurityRole()
    {
    }

    public OrgSecurityRole getOrgSecurityRole()
    {
        return this.orgSecurityRole;
    }

    public boolean hasPrivilege(Privilege privilege)
    {
        return orgSecurityRole.hasPrivilege(privilege);
    }
}
