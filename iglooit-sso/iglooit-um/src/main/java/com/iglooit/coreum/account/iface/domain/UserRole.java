package com.iglooit.coreum.account.iface.domain;

import com.iglooit.commons.iface.annotation.EncryptedString;
import com.iglooit.commons.iface.domain.Encryptable;
import com.iglooit.commons.iface.type.UUID;
import com.iglooit.coreum.security.iface.access.domain.Privilege;
import com.iglooit.coreum.security.iface.access.domain.SecurityRole;
import com.iglooit.coreum.security.iface.access.domain.Subject;
import org.hibernate.annotations.Cascade;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "USER_ROLES")
public class UserRole extends PartyRole implements Subject, Encryptable, Serializable
{
    @OneToMany(mappedBy = "userRole", cascade = CascadeType.ALL)
    @Cascade({org.hibernate.annotations.CascadeType.DELETE_ORPHAN})
    private Set<UserOrgSecurityRole> userOrgSecurityRoles;

    @Column(name = "USER_USERNAME", nullable = false)
    private String username;

    @Column(name = "USER_PASSWORD", nullable = true)
    @EncryptedString
    private String password;

    @Column(name = "USER_ACCESSPROFILE", nullable = true)
    private String accessProfile;

    @Column(name = "USER_ENDDATE", nullable = true)
    private Date endDate;

    @Column(name = "USER_PASSWORDDEFNAME", nullable = false)
    private String passwordDefName;

    @Column(name = "USER_PREFERREDNOTIFICATION", nullable = true)
    private String preferredNotification;

    @Column(name = "USER_PREFERREDLOCALE", nullable = true)
    private String preferredLocale;

    @Column(name = "USER_OSSEMPLOYEE_ID", nullable = true)
    private String ossEmployeeId;

    @Column(name = "USER_CREATEDATE", nullable = true)
    private Date createDate;

    //isActive, in conjunction with the endDate indicates whether the scheduler needs to deactivate
    //this UserRole
    @Column(name = "USER_ISACTIVE", nullable = false)
    private boolean isActive;

    public UserRole()
    {
        isActive = true;
    }

    public UserRole(Party party, PartyRoleType roleType, String username, String password, String accessProfile,
                    String passwordDefName, Date endDate, String preNotification, String ossEmployeeId)
    {
        super(party, roleType);
        this.username = username;
        this.password = password;
        this.accessProfile = accessProfile;
        this.passwordDefName = passwordDefName;
        this.endDate = endDate;
        this.preferredNotification = preNotification;
        this.ossEmployeeId = ossEmployeeId;
        isActive = true;
    }

    public UserRole(PartyRoleType roleType, String username, String password, String accessProfile,
                    String passwordDefName, Date endDate, String preNotification, String ossEmployeeId)
    {
        super(roleType);
        this.username = username;
        this.password = password;
        this.accessProfile = accessProfile;
        this.passwordDefName = passwordDefName;
        this.endDate = endDate;
        this.preferredNotification = preNotification;
        this.ossEmployeeId = ossEmployeeId;
    }

    public UserRole(PartyRoleType roleType, String username, String password)
    {
        super(roleType);
        this.username = username;
        this.password = password;
    }

    public UserRole(UUID id, Party party, PartyRoleType roleType, String username, String password,
                    String accessProfile, String passwordDefName, Date endDate, String preNotification)
    {
        super(id, party, roleType);
        this.username = username;
        this.password = password;
        this.accessProfile = accessProfile;
        this.passwordDefName = passwordDefName;
        this.endDate = endDate;
        this.preferredNotification = preNotification;
    }


    @Transient
    public String retrieveEncryptionKey()
    {
        //NB - We assume here that the UserRole's party is an individual, which we're not currently enforcing
        Organization org = getIndividual().getVendorOrganization();
        return org.getEncryptionKey();
    }

    @Transient
    public String retrieveEncryptionCipher()
    {
        Organization org = getIndividual().getVendorOrganization();
        return org.getEncryptionCipher();
    }


    /**
     * Get all of the security roles name associated with this user role.
     */
    public List<String> getActiveSecurityRoles()
    {
        ArrayList<String> securityRoles = new ArrayList<String>();

        for (UserOrgSecurityRole userOrgSecurityRole : getUserOrgSecurityRoles())
        {
            SecurityRole securityRole = userOrgSecurityRole.getOrgSecurityRole().getSecurityRole();
            if (securityRole.getActive())
                securityRoles.add(securityRole.getName());
        }

        return Collections.unmodifiableList(securityRoles);
    }


    public Set<UserOrgSecurityRole> getUserOrgSecurityRoles()
    {
        return userOrgSecurityRoles == null ? Collections.EMPTY_SET : userOrgSecurityRoles;
    }

    public String getUsername()
    {
        return this.username;
    }

    public String getPassword()
    {
        return this.password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public boolean hasPrivilege(Privilege applicablePrivilege)
    {
        for (UserOrgSecurityRole userOrgSecurityRole : getUserOrgSecurityRoles())
        {
            if (userOrgSecurityRole.hasPrivilege(applicablePrivilege))
                return true;
        }
        return false;
    }

    public Individual getIndividual()
    {
        return super.getIndividual();
    }

    public Organization getOrganization()
    {
        return super.getOrganization();
    }

    public boolean isActive()
    {
        return isActive;
    }

    public boolean isDisabledNow(Date currentDate)
    {
        //note that the 'active-ness' is not taken inot account, as it is a flag for the schedulded task to act upon
        //not the primary indicator of whether a user is enabled or not.
        return endDate != null && endDate.before(currentDate);
    }

    @Override
    public String toString()
    {
        return "[UserRole: id: " + getId() + " login: " + getUsername() + " name: " + getIndividual().getName() + "]";
    }

    /**
     * Get all of the security roles associated with this user role.
     */
    public List<SecurityRole> getSecurityRoles()
    {
        ArrayList<SecurityRole> securityRoles = new ArrayList<SecurityRole>();

        for (UserOrgSecurityRole userOrgSecurityRole : getUserOrgSecurityRoles())
            securityRoles.add(userOrgSecurityRole.getOrgSecurityRole().getSecurityRole());

        return Collections.unmodifiableList(securityRoles);
    }
}
