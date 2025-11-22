package com.crm.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Lead - Sales leads
 */
@Entity
@Table(name = "lead")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Lead implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "lead_number")
    private String leadNumber;

    @Column(name = "annual_revenue")
    private Integer annualRevenue;

    @Column(name = "subsid")
    private Integer subsid;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(
        value = {
            "addresses",
            "company",
            "user",
            "customerType",
            "customerStatus",
            "ownershipType",
            "industryType",
            "customerCategory",
            "paymentTerms",
            "invoiceFrequency",
            "gstTreatment",
            "outstandingPerson",
            "department",
            "tenat",
            "masterCategories",
        },
        allowSetters = true
    )
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterStaticType leadSource;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterStaticType industryType;

    @ManyToOne(fetch = FetchType.LAZY)
    private MasterStaticType leadStatus;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Lead id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Lead name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLeadNumber() {
        return this.leadNumber;
    }

    public Lead leadNumber(String leadNumber) {
        this.setLeadNumber(leadNumber);
        return this;
    }

    public void setLeadNumber(String leadNumber) {
        this.leadNumber = leadNumber;
    }

    public Integer getAnnualRevenue() {
        return this.annualRevenue;
    }

    public Lead annualRevenue(Integer annualRevenue) {
        this.setAnnualRevenue(annualRevenue);
        return this;
    }

    public void setAnnualRevenue(Integer annualRevenue) {
        this.annualRevenue = annualRevenue;
    }

    // ✅ Getter / setter / builder for subsid
    public Integer getSubsid() {
        return this.subsid;
    }

    public void setSubsid(Integer subsid) {
        this.subsid = subsid;
    }

    public Lead subsid(Integer subsid) {
        this.setSubsid(subsid);
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Lead user(User user) {
        this.setUser(user);
        return this;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Lead customer(Customer customer) {
        this.setCustomer(customer);
        return this;
    }

    public MasterStaticType getLeadSource() {
        return this.leadSource;
    }

    public void setLeadSource(MasterStaticType masterStaticType) {
        this.leadSource = masterStaticType;
    }

    public Lead leadSource(MasterStaticType masterStaticType) {
        this.setLeadSource(masterStaticType);
        return this;
    }

    public MasterStaticType getIndustryType() {
        return this.industryType;
    }

    public void setIndustryType(MasterStaticType masterStaticType) {
        this.industryType = masterStaticType;
    }

    public Lead industryType(MasterStaticType masterStaticType) {
        this.setIndustryType(masterStaticType);
        return this;
    }

    public MasterStaticType getLeadStatus() {
        return this.leadStatus;
    }

    public void setLeadStatus(MasterStaticType masterStaticType) {
        this.leadStatus = masterStaticType;
    }

    public Lead leadStatus(MasterStaticType masterStaticType) {
        this.setLeadStatus(masterStaticType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Lead)) {
            return false;
        }
        return getId() != null && getId().equals(((Lead) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Lead{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", leadNumber='" + getLeadNumber() + "'" +
            ", annualRevenue=" + getAnnualRevenue() +
            ", subsid=" + getSubsid() +
            "}";
    }
}
