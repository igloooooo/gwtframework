package com.iglooit.coreum.account.iface.domain;

import com.iglooit.commons.iface.type.UUID;
import com.iglooit.coreum.base.iface.domain.JpaDomainEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "PARTY_ROLES")
@Inheritance(strategy = InheritanceType.JOINED)
public class PartyRole extends JpaDomainEntity<PartyRole> implements PartyEntityVisitor
{
    private enum PartyRoleRelationshipType
    {
        CHILD, PARENT
    }

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.PERSIST})
    @JoinColumn(name = "PARR_PART_ID", nullable = false)
    private Party party;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "PARR_TYPE_ID", nullable = false)
    private PartyRoleType partyRoleType;

    @OneToMany(mappedBy = "child", cascade = CascadeType.ALL)
    private List<PartyRoleInteraction> parentRoles = new ArrayList<PartyRoleInteraction>();

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    private List<PartyRoleInteraction> childRoles = new ArrayList<PartyRoleInteraction>();

    @Column(name = "PARR_NAME", nullable = true, insertable = false, updatable = false)
    private String partyRoleName = null;

    public PartyRole()
    {
    }

    public PartyRole(UUID id, Party party, PartyRoleType roleType)
    {
        super(id);
        this.party = party;
        this.partyRoleType = roleType;
    }

    public PartyRole(Party party, PartyRoleType roleType)
    {
        this.party = party;
        this.partyRoleType = roleType;
    }

    public PartyRole(PartyRoleType roleType)
    {
        this.partyRoleType = roleType;
    }


    //so modify visitor and remove transient if needed later, for code safety reason
    @Transient
    private transient Individual individual;
    @Transient
    private transient Organization organization;

    public Party getParty()
    {
        return this.party;
    }

    public void setParty(Party party)
    {
        this.party = party;
    }

    public Individual getIndividual()
    {
        this.party.accept(this);
        return this.individual;
    }

    public Organization getOrganization()
    {
        this.party.accept(this);
        return this.organization;
    }

    public void visit(Individual individual)
    {
        this.individual = individual;
        this.organization = null;
    }

    public void visit(Organization organization)
    {
        this.organization = organization;
        this.individual = null;
    }
}
