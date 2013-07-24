package com.iglooit.coreum.account.iface.domain;

import com.iglooit.commons.iface.type.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;


/**
 * Organization represents a collection of people in the system. Organizations can be used to represent companies,
 * non-rofits, as well as potentially departments within another organization.
 * <p/>
 * Organization inherits from Party, with the inheritance mapping achieved via a one-to-one link between their
 * respective id columns
 */
@Entity
@Table(name = "ORGANIZATIONS")
public class Organization extends Party<Organization>
{
    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    private List<OrgSecurityRole> roles;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    private List<Individual> individuals;

    @OneToMany(mappedBy = "organization", cascade = CascadeType.ALL)
    private List<OrgEntryPoint> entryPoints;

    @Column(name = "ORGA_ENCRYPTIONKEY", nullable = true)
    private String encryptionKey;

    @Column(name = "ORGA_ENCRYPTIONCIPHER", nullable = true)
    private String encryptionCipher;

    public Organization()
    {
    }

    public Organization(String name, String description)
    {
        super(name, description);
    }

    public Organization(String name, String desc, String encryptionKey)
    {
        super(name, desc);
        this.encryptionKey = encryptionKey;
    }

    public Organization(String name, String desc, String encryptionKey, String encryptionCipher)
    {
        this(name, desc, encryptionKey);
        this.encryptionCipher = encryptionCipher;
    }

    public Organization(UUID id, String name, String description)
    {
        super(id, name, description);
    }

    public String getEncryptionKey()
    {
        return encryptionKey;
    }

    public String getEncryptionCipher()
    {
        return encryptionCipher;
    }

    public void accept(PartyEntityVisitor visitor)
    {
        visitor.visit(this);
    }

    @Override
    public String toString()
    {
        return "[Organization: [name: " + getName() +
            " description: " + getDescription() + "]]";
    }
}
