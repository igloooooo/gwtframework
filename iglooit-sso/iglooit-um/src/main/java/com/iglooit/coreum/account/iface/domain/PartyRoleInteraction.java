package com.iglooit.coreum.account.iface.domain;

import com.iglooit.commons.iface.type.UUID;
import com.iglooit.coreum.base.iface.domain.JpaDomainEntity;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "PARTY_ROLE_INTERACTIONS")
public class PartyRoleInteraction extends JpaDomainEntity<PartyRoleInteraction>
{
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "PARI_PARR_PARENTID", nullable = true)
    private PartyRole parent;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "PARI_PARR_CHILDID", nullable = true)
    private PartyRole child;

    public PartyRoleInteraction()
    {
    }

    public PartyRoleInteraction(PartyRole parent, PartyRole child)
    {
        this.parent = parent;
        this.child = child;
    }

    public PartyRoleInteraction(UUID id, PartyRole parent, PartyRole child)
    {
        super(id);
        this.parent = parent;
        this.child = child;
    }

    protected PartyRole getParent()
    {
        return parent;
    }

    protected void setParent(PartyRole parent)
    {
        this.parent = parent;
    }

    protected PartyRole getChild()
    {
        return child;
    }

    protected void setChild(PartyRole child)
    {
        this.child = child;
    }

    public String getParentName()
    {
        return this.parent.getParty().getName();
    }

    public String getChildName()
    {
        return this.child.getParty().getName();
    }
}
