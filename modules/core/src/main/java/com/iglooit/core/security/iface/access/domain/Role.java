package com.iglooit.core.security.iface.access.domain;

import com.clarity.commons.iface.domain.Validator;
import com.clarity.commons.iface.domain.meta.Meta;
import com.clarity.commons.iface.type.UUID;
import com.clarity.core.base.iface.domain.NamedJpaDomainEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Role extends NamedJpaDomainEntity<Role>
{
    //special role which has access to everything across all Accounts.
    //note that all other roles are configurable.
    private static final String SUPER_USER = "root";

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<RolePrivilege> rolePrivileges;

    protected Meta createMetaDelegate()
    {
        return new RoleMeta(this);
    }

    public Role()
    {
    }

    protected List<RolePrivilege> getRolePrivileges()
    {
        return rolePrivileges;
    }

    protected void setRolePrivileges(List<RolePrivilege> rolePrivileges)
    {
        this.rolePrivileges = rolePrivileges;
    }

    public List<Validator> doGetValidators()
    {
        return new ArrayList<Validator>();
    }

    public Role(String name, String description)
    {
        super(name, description);
    }

    public Role(UUID id, String name, String description)
    {
        super(id, name, description);
    }

    public boolean hasPrivilege(Privilege privilege)
    {
        for (RolePrivilege rolePrivilege : rolePrivileges)
        {
            if (rolePrivilege.hasPrivilege(privilege))
                return true;
        }
        return false;
    }

    @Override
    public String toString()
    {
        return "[Role: [name: " + getName() +
            " description: " + getDescription() + "]]";
    }
}
