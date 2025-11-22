package com.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Tenant.
 */
@Entity
@Table(name = "tenant")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Tenant implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "company_name", nullable = false)
    private String companyName;

    @Column(name = "contact_person")
    private String contactPerson;

    @Column(name = "logo")
    private String logo;

    @Column(name = "website")
    private String website;

    @Column(name = "registration_number")
    private String registrationNumber;

    @Column(name = "sub_id")
    private Integer subId;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tenant")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "city", "state", "country", "customer", "tenant" }, allowSetters = true)
    private Set<Address> addresses = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_tenant__users",
        joinColumns = @JoinColumn(name = "tenant_id"),
        inverseJoinColumns = @JoinColumn(name = "users_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<User> users = new HashSet<>();

    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "tenant")
    private Encryption encryption;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Tenant id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompanyName() {
        return this.companyName;
    }

    public Tenant companyName(String companyName) {
        this.setCompanyName(companyName);
        return this;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContactPerson() {
        return this.contactPerson;
    }

    public Tenant contactPerson(String contactPerson) {
        this.setContactPerson(contactPerson);
        return this;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getLogo() {
        return this.logo;
    }

    public Tenant logo(String logo) {
        this.setLogo(logo);
        return this;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getWebsite() {
        return this.website;
    }

    public Tenant website(String website) {
        this.setWebsite(website);
        return this;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public String getRegistrationNumber() {
        return this.registrationNumber;
    }

    public Tenant registrationNumber(String registrationNumber) {
        this.setRegistrationNumber(registrationNumber);
        return this;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    public Integer getSubId() {
        return this.subId;
    }

    public Tenant subId(Integer subId) {
        this.setSubId(subId);
        return this;
    }

    public void setSubId(Integer subId) {
        this.subId = subId;
    }

    public Set<Address> getAddresses() {
        return this.addresses;
    }

    public void setAddresses(Set<Address> addresses) {
        if (this.addresses != null) {
            this.addresses.forEach(i -> i.setTenant(null));
        }
        if (addresses != null) {
            addresses.forEach(i -> i.setTenant(this));
        }
        this.addresses = addresses;
    }

    public Tenant addresses(Set<Address> addresses) {
        this.setAddresses(addresses);
        return this;
    }

    public Tenant addAddresses(Address address) {
        this.addresses.add(address);
        address.setTenant(this);
        return this;
    }

    public Tenant removeAddresses(Address address) {
        this.addresses.remove(address);
        address.setTenant(null);
        return this;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public Tenant users(Set<User> users) {
        this.setUsers(users);
        return this;
    }

    public Tenant addUsers(User user) {
        this.users.add(user);
        return this;
    }

    public Tenant removeUsers(User user) {
        this.users.remove(user);
        return this;
    }

    public Encryption getEncryption() {
        return this.encryption;
    }

    public void setEncryption(Encryption encryption) {
        if (this.encryption != null) {
            this.encryption.setTenant(null);
        }
        if (encryption != null) {
            encryption.setTenant(this);
        }
        this.encryption = encryption;
    }

    public Tenant encryption(Encryption encryption) {
        this.setEncryption(encryption);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Tenant)) {
            return false;
        }
        return getId() != null && getId().equals(((Tenant) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Tenant{" +
            "id=" + getId() +
            ", companyName='" + getCompanyName() + "'" +
            ", contactPerson='" + getContactPerson() + "'" +
            ", logo='" + getLogo() + "'" +
            ", website='" + getWebsite() + "'" +
            ", registrationNumber='" + getRegistrationNumber() + "'" +
            ", subId=" + getSubId() +
            "}";
    }
}
