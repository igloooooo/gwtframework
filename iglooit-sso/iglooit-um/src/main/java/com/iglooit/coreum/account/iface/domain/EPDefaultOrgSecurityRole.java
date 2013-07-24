package com.iglooit.coreum.account.iface.domain;

import com.iglooit.coreum.base.iface.domain.JpaDomainEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "EP_DEFAULT_ORG_SECURITYROLES")
public class EPDefaultOrgSecurityRole extends JpaDomainEntity<EPDefaultOrgSecurityRole>
{
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EDOS_OREP_ID", nullable = false)
    private OrgEntryPoint orgEntryPoint;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "EDOS_ORSR_ID", nullable = false)
    private OrgSecurityRole orgSecurityRole;

    public EPDefaultOrgSecurityRole()
    {
    }

    public EPDefaultOrgSecurityRole(OrgSecurityRole orgSecurityRole)
    {
        this.orgSecurityRole = orgSecurityRole;
    }

    public OrgSecurityRole getOrgSecurityRole()
    {
        return orgSecurityRole;
    }

    public void setOrgSecurityRole(OrgSecurityRole orgSecurityRole)
    {
        this.orgSecurityRole = orgSecurityRole;
    }

    public OrgEntryPoint getOrgEntryPoint()
    {
        return orgEntryPoint;
    }

    public void setOrgEntryPoint(OrgEntryPoint orgEntryPoint)
    {
        this.orgEntryPoint = orgEntryPoint;
    }
}
