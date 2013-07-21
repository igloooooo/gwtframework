package com.iglooit.core.security.iface.access.domain;

import com.clarity.commons.iface.domain.Validator;
import com.clarity.commons.iface.domain.meta.Meta;
import com.clarity.commons.iface.type.UUID;
import com.clarity.core.base.iface.domain.JpaDomainEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ROLE_PRIVILEGES")
public class RolePrivilege extends JpaDomainEntity<RolePrivilege>
{
    @ManyToOne
    @JoinColumn(name = "ROLP_SECR_ID", nullable = false)
    private SecurityRole securityRole;

    @ManyToOne
    @JoinColumn(name = "ROLP_PRIV_ID", nullable = false)
    private Privilege privilege;

    public RolePrivilege()
    {
    }

    public SecurityRole getSecurityRole()
    {
        return securityRole;
    }

    public void setSecurityRole(SecurityRole securityRole)
    {
        this.securityRole = securityRole;
    }

    public Privilege getPrivilege()
    {
        return privilege;
    }

    public void setPrivilege(Privilege privilege)
    {
        this.privilege = privilege;
    }

    public List<Validator> doGetValidators()
    {
        return new ArrayList<Validator>();
    }

    public RolePrivilege(SecurityRole securityRole, Privilege privilege)
    {
        this.securityRole = securityRole;
        this.privilege = privilege;
    }

    public RolePrivilege(UUID id, SecurityRole securityRole, Privilege privilege)
    {
        super(id);
        this.securityRole = securityRole;
        this.privilege = privilege;
    }

    public boolean hasPrivilege(Privilege privilege)
    {
        return (privilege.equals(this.privilege));
    }

    protected Meta createMetaDelegate()
    {
        return new RolePrivilegeMeta(this);
    }
}
