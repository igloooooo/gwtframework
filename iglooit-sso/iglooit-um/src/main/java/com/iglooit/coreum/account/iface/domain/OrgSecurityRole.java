package com.iglooit.coreum.account.iface.domain;

import com.iglooit.coreum.base.iface.domain.JpaDomainEntity;
import com.iglooit.coreum.security.iface.access.domain.Privilege;
import com.iglooit.coreum.security.iface.access.domain.SecurityRole;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "ORG_SECURITY_ROLES")
public class OrgSecurityRole extends JpaDomainEntity<OrgSecurityRole>
{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORSR_ROLE_ID", nullable = false)
    private SecurityRole securityRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ORGA_ID", nullable = false)
    private Organization organization;

    @Column(name = "ORSR_DEFAULT_ROLE", nullable = false)
    private Boolean defaultRole;

    public OrgSecurityRole()
    {
    }

    public SecurityRole getSecurityRole()
    {
        return securityRole;
    }

    public boolean hasPrivilege(Privilege privilege)
    {
        return securityRole.hasPrivilege(privilege);
    }
}
