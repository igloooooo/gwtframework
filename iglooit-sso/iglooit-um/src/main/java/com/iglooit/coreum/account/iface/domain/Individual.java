package com.iglooit.coreum.account.iface.domain;

import com.iglooit.coreum.security.iface.access.domain.Privilege;
import com.iglooit.coreum.security.iface.access.domain.Subject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * An Individual is a Party that corresponds to a person (as opposed to an Organization). This structure is based on the
 * SID entity of the same name.
 */
@Entity
@Table(name = "individuals")
public class Individual extends Party<Individual> implements Subject
{
    @Column(name = "indi_telephone", nullable = true)
    private String telephone;
    @Column(name = "indi_mobile", nullable = true)
    private String mobile;
    @Column(name = "indi_faxnumber", nullable = true)
    private String faxNumber;
    @Column(name = "indi_email", nullable = true)
    private String email;
    @Column(name = "indi_emailpassword", nullable = true)
    private String emailPassword;

    //if an individual has enabled set to false then they may not log into the system.
    @Column(name = "indi_enabled")
    private Boolean enabled;

    //an individual may be a member of an organization.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "indi_orga_id")
    private Organization organization;

    public Individual()
    {
    }

    public boolean hasPrivilege(Privilege applicablePrivilege)
    {
        UserRole userRole = null;
        for (PartyRole role : super.getPartyRoles())
            if (role instanceof UserRole)
                userRole = (UserRole)role;

        for (UserOrgSecurityRole userOrgSecurityRole : userRole.getUserOrgSecurityRoles())
            if (userOrgSecurityRole.hasPrivilege(applicablePrivilege))
                return true;

        return false;
    }

    public Organization getVendorOrganization()
    {
        return getOrganization();
    }


    public Organization getIndividualOrganization()
    {
        return getOrganization();
    }

    @Override
    public String toString()
    {
        return "[Individual: [name: " + getName() +
            " description: " + getDescription() + "]]";
    }

    public void accept(PartyEntityVisitor visitor)
    {
        visitor.visit(this);
    }

    public Organization getOrganization()
    {
        return organization;
    }
}
