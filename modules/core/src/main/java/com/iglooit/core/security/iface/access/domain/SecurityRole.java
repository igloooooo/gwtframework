package com.iglooit.core.security.iface.access.domain;

import com.clarity.commons.iface.domain.Validator;
import com.clarity.commons.iface.domain.meta.Meta;
import com.clarity.commons.iface.type.Option;
import com.clarity.commons.iface.type.UUID;
import com.clarity.core.base.iface.domain.NamedJpaDomainEntity;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "SECURITY_ROLES")
public class SecurityRole extends NamedJpaDomainEntity<SecurityRole>
{
    //special role which has access to everything across all Accounts.
    //note that all other roles are configurable.
    private static final String SUPER_USER = "root";
    private static final String WEB_ADMIN_ROLE = "WEB_ADMIN";

    @OneToMany(mappedBy = "securityRole", cascade = CascadeType.ALL)
    private Set<RolePrivilege> rolePrivileges;

    @Column(name = "SECR_TYPE", nullable = false)
    private String securityRoleType;

    @Column(name = "SECR_ISACTIVE", columnDefinition = "varchar2(1)", nullable = false)
    @Type(type = "yes_no")
    private Boolean isActive;

    public static final String OSS_ROLE_TYPE = "oss";
    public static final String BSS_ROLE_TYPE = "bss";

    public SecurityRole()
    {
    }

    public Set<RolePrivilege> getRolePrivileges()
    {
        return rolePrivileges;
    }

    protected void setRolePrivileges(Set<RolePrivilege> rolePrivileges)
    {
        this.rolePrivileges = rolePrivileges;
    }

    public List<Validator> doGetValidators()
    {
        return new ArrayList<Validator>();
    }

    public SecurityRole(String name, String description, String type)
    {
        super(name, description);
        this.securityRoleType = type;
        this.isActive = Boolean.TRUE;
    }

    public SecurityRole(UUID id, String name, String description, String type)
    {
        super(id, name, description);
        this.securityRoleType = type;
        this.isActive = Boolean.TRUE;
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

    public String getSecurityRoleType()
    {
        return securityRoleType;
    }

    public void setSecurityRoleType(String securityRoleType)
    {
        this.securityRoleType = securityRoleType;
    }


    public boolean isOssRole()
    {
        return OSS_ROLE_TYPE.equals(securityRoleType);
    }

    public boolean isBssRole()
    {
        return BSS_ROLE_TYPE.equals(securityRoleType);
    }

    public Option<String> getOssRoleName()
    {
        if (isOssRole())
            return Option.some(getName());
        else
            return Option.none();
    }

    public Boolean isActive()
    {
        return isActive;
    }

    public void setActive(Boolean active)
    {
        isActive = active;
    }

    protected Meta createMetaDelegate()
    {
        return new SecurityRoleMeta(this);
    }

    public boolean isAdminRole()
    {
        return WEB_ADMIN_ROLE.equals(getName());
    }
}
