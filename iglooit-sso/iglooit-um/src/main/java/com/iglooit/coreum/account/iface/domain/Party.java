package com.iglooit.coreum.account.iface.domain;

import com.iglooit.commons.iface.type.UUID;
import com.iglooit.coreum.base.iface.domain.NamedJpaDomainEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Party is the root entity in an inheritance hierarchy that is used model people and organisations in the system.
 * <p/>
 * Note that there are one to one relatonships in the db between the party and orgainization and party and individual
 * tables. The one to ones are mapped by sharing the same primary key value.
 * <p/>
 * In the class structure however these one to ones are represented using inheritance.
 * <p/>
 * Note also that this structure is based heavily on the SID.
 */
@Entity
@Table(name = "PARTIES")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Party<InheritingClass extends NamedJpaDomainEntity> extends NamedJpaDomainEntity<InheritingClass>
    implements PartyEntity
{
    @OneToMany(mappedBy = "party", cascade = CascadeType.ALL)
    private List<PartyRole> partyRoles;

    public Party()
    {
    }

    protected Party(String name, String desc)
    {
        super(name, desc);
    }

    protected Party(UUID id, String name, String desc)
    {
        super(id, name, desc);
    }

    public List<PartyRole> getPartyRoles()
    {
        return this.partyRoles;
    }

    public void setPartyRoles(List<PartyRole> partyRoles)
    {
        this.partyRoles = partyRoles;
    }

    public <T extends PartyRole> T addPartyRole(T role)
    {
        if (partyRoles == null)
        {
            partyRoles = new ArrayList<PartyRole>();
        }
        partyRoles.add(role);
        role.setParty(this);

        return role;
    }

    public PartyRole addPartyRole(PartyRoleType type)
    {
        return addPartyRole(new PartyRole(this, type));
    }
}
