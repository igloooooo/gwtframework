package com.iglooit.coreum.security.iface.access.domain;

import com.iglooit.coreum.base.iface.domain.NamedJpaDomainEntity;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name = "SECURITY_ROLES")
public class SecurityRole extends NamedJpaDomainEntity<SecurityRole>
{
    //special role which has access to everything across all Accounts.
    //note that all other roles are configurable.
    private static final String SUPER_USER = "root";

    @OneToMany(mappedBy = "securityRole", cascade = CascadeType.ALL)
    private Set<RolePrivilege> rolePrivileges;

    @Column(name = "SECR_TYPE", nullable = false)
    private String securityRoleType;

    @Column(name = "SECR_ISACTIVE", columnDefinition = "varchar2(1)", nullable = false)
    @Type(type = "yes_no")
    private Boolean isActive;

    public SecurityRole()
    {
    }

    public boolean hasPrivilege(Privilege privilege)
    {
        if (rolePrivileges != null)
            for (RolePrivilege rolePrivilege : rolePrivileges)
                if (rolePrivilege.hasPrivilege(privilege))
                    return true;
        return false;
    }

    @Override
    public String toString()
    {
        return "[SecurityRole: [name: " + getName() +
            " description: " + getDescription() + "]]";
    }

    public Boolean getActive()
    {
        return isActive;
    }
}
