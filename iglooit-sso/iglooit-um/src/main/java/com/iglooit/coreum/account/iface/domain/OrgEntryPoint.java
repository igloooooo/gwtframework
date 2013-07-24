package com.iglooit.coreum.account.iface.domain;

import com.iglooit.coreum.base.iface.domain.NamedJpaDomainEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "ORG_ENTRY_POINTS")
public class OrgEntryPoint extends NamedJpaDomainEntity<OrgEntryPoint>
{

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "OREP_ORGA_ID", nullable = false)
    private Organization organization;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "OREP_ANUR_ID", nullable = false)
    private AnonymousUserRole anonymousUserRole;

    @OneToMany(mappedBy = "orgEntryPoint", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<EPDefaultOrgSecurityRole> entryPointDefaultOrgSecurityRoles;

    public OrgEntryPoint()
    {
    }

    public OrgEntryPoint(String name, String description, AnonymousUserRole anonymousUserRole)
    {
        super(name, description);
        this.anonymousUserRole = anonymousUserRole;
    }

    public List<EPDefaultOrgSecurityRole> getEntryPointDefaultOrgSecurityRoles()
    {
        return entryPointDefaultOrgSecurityRoles;
    }

    public void setEntryPointDefaultOrgSecurityRoles(List<EPDefaultOrgSecurityRole>
                                                         entryPointDefaultOrgSecurityRoles)
    {
        this.entryPointDefaultOrgSecurityRoles = entryPointDefaultOrgSecurityRoles;
    }

    public AnonymousUserRole getAnonymousUserRole()
    {
        return anonymousUserRole;
    }

    public void setAnonymousUserRole(AnonymousUserRole anonymousUserRole)
    {
        this.anonymousUserRole = anonymousUserRole;
    }

    public Organization getOrganization()
    {
        return organization;
    }

    public void setOrganization(Organization organization)
    {
        this.organization = organization;
    }
}
