package com.iglooit.coreum.security.iface.access.domain;

import com.iglooit.coreum.base.iface.domain.JpaDomainEntity;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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

    public boolean hasPrivilege(Privilege privilege)
    {
        return (privilege.equals(this.privilege));
    }
}
