package com.mgs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mgs.domain.enumeration.CustomerStatus;
import com.mgs.domain.enumeration.CustomerType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Customer.
 */
@Entity
@Table(name = "customer")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Customer extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Lob
    @Column(name = "name", nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type")
    private CustomerType customerType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private CustomerStatus status;

    @Size(max = 255)
    @Column(name = "segment", length = 255)
    private String segment;

    @Size(max = 255)
    @Column(name = "industry", length = 255)
    private String industry;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tenantProfile", "tenantBranding" }, allowSetters = true)
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant", "headUser" }, allowSetters = true)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant", "tenantProfile" }, allowSetters = true)
    private Address billingAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant", "tenantProfile" }, allowSetters = true)
    private Address shippingAddress;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant", "customer", "address", "ownerUser" }, allowSetters = true)
    private Contact primaryContact;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Customer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Customer name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CustomerType getCustomerType() {
        return this.customerType;
    }

    public Customer customerType(CustomerType customerType) {
        this.setCustomerType(customerType);
        return this;
    }

    public void setCustomerType(CustomerType customerType) {
        this.customerType = customerType;
    }

    public CustomerStatus getStatus() {
        return this.status;
    }

    public Customer status(CustomerStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(CustomerStatus status) {
        this.status = status;
    }

    public String getSegment() {
        return this.segment;
    }

    public Customer segment(String segment) {
        this.setSegment(segment);
        return this;
    }

    public void setSegment(String segment) {
        this.segment = segment;
    }

    public String getIndustry() {
        return this.industry;
    }

    public Customer industry(String industry) {
        this.setIndustry(industry);
        return this;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public Tenant getTenant() {
        return this.tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public Customer tenant(Tenant tenant) {
        this.setTenant(tenant);
        return this;
    }

    public Department getDepartment() {
        return this.department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Customer department(Department department) {
        this.setDepartment(department);
        return this;
    }

    public Address getBillingAddress() {
        return this.billingAddress;
    }

    public void setBillingAddress(Address address) {
        this.billingAddress = address;
    }

    public Customer billingAddress(Address address) {
        this.setBillingAddress(address);
        return this;
    }

    public Address getShippingAddress() {
        return this.shippingAddress;
    }

    public void setShippingAddress(Address address) {
        this.shippingAddress = address;
    }

    public Customer shippingAddress(Address address) {
        this.setShippingAddress(address);
        return this;
    }

    public Contact getPrimaryContact() {
        return this.primaryContact;
    }

    public void setPrimaryContact(Contact contact) {
        this.primaryContact = contact;
    }

    public Customer primaryContact(Contact contact) {
        this.setPrimaryContact(contact);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Customer)) {
            return false;
        }
        return getId() != null && getId().equals(((Customer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Customer{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", customerType='" + getCustomerType() + "'" +
            ", status='" + getStatus() + "'" +
            ", segment='" + getSegment() + "'" +
            ", industry='" + getIndustry() + "'" +
            "}";
    }
}
