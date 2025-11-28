package com.mgs.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Plan.
 */
@Entity
@Table(name = "plan")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Plan extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "code", length = 50, nullable = false)
    private String code;

    @NotNull
    @Size(max = 100)
    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "max_users")
    private Integer maxUsers;

    @Column(name = "max_storage_mb")
    private Integer maxStorageMb;

    @Column(name = "max_customers")
    private Integer maxCustomers;

    @Column(name = "max_contacts")
    private Integer maxContacts;

    @Column(name = "max_quotations")
    private Integer maxQuotations;

    @Column(name = "max_complaints")
    private Integer maxComplaints;

    @NotNull
    @Column(name = "price_per_month", precision = 21, scale = 2, nullable = false)
    private BigDecimal pricePerMonth;

    @NotNull
    @Size(max = 10)
    @Column(name = "currency", length = 10, nullable = false)
    private String currency;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Plan id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return this.code;
    }

    public Plan code(String code) {
        this.setCode(code);
        return this;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return this.name;
    }

    public Plan name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Plan description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMaxUsers() {
        return this.maxUsers;
    }

    public Plan maxUsers(Integer maxUsers) {
        this.setMaxUsers(maxUsers);
        return this;
    }

    public void setMaxUsers(Integer maxUsers) {
        this.maxUsers = maxUsers;
    }

    public Integer getMaxStorageMb() {
        return this.maxStorageMb;
    }

    public Plan maxStorageMb(Integer maxStorageMb) {
        this.setMaxStorageMb(maxStorageMb);
        return this;
    }

    public void setMaxStorageMb(Integer maxStorageMb) {
        this.maxStorageMb = maxStorageMb;
    }

    public Integer getMaxCustomers() {
        return this.maxCustomers;
    }

    public Plan maxCustomers(Integer maxCustomers) {
        this.setMaxCustomers(maxCustomers);
        return this;
    }

    public void setMaxCustomers(Integer maxCustomers) {
        this.maxCustomers = maxCustomers;
    }

    public Integer getMaxContacts() {
        return this.maxContacts;
    }

    public Plan maxContacts(Integer maxContacts) {
        this.setMaxContacts(maxContacts);
        return this;
    }

    public void setMaxContacts(Integer maxContacts) {
        this.maxContacts = maxContacts;
    }

    public Integer getMaxQuotations() {
        return this.maxQuotations;
    }

    public Plan maxQuotations(Integer maxQuotations) {
        this.setMaxQuotations(maxQuotations);
        return this;
    }

    public void setMaxQuotations(Integer maxQuotations) {
        this.maxQuotations = maxQuotations;
    }

    public Integer getMaxComplaints() {
        return this.maxComplaints;
    }

    public Plan maxComplaints(Integer maxComplaints) {
        this.setMaxComplaints(maxComplaints);
        return this;
    }

    public void setMaxComplaints(Integer maxComplaints) {
        this.maxComplaints = maxComplaints;
    }

    public BigDecimal getPricePerMonth() {
        return this.pricePerMonth;
    }

    public Plan pricePerMonth(BigDecimal pricePerMonth) {
        this.setPricePerMonth(pricePerMonth);
        return this;
    }

    public void setPricePerMonth(BigDecimal pricePerMonth) {
        this.pricePerMonth = pricePerMonth;
    }

    public String getCurrency() {
        return this.currency;
    }

    public Plan currency(String currency) {
        this.setCurrency(currency);
        return this;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public Plan isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Plan)) {
            return false;
        }
        return getId() != null && getId().equals(((Plan) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Plan{" +
            "id=" + getId() +
            ", code='" + getCode() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", maxUsers=" + getMaxUsers() +
            ", maxStorageMb=" + getMaxStorageMb() +
            ", maxCustomers=" + getMaxCustomers() +
            ", maxContacts=" + getMaxContacts() +
            ", maxQuotations=" + getMaxQuotations() +
            ", maxComplaints=" + getMaxComplaints() +
            ", pricePerMonth=" + getPricePerMonth() +
            ", currency='" + getCurrency() + "'" +
            ", isActive='" + getIsActive() + "'" +
            "}";
    }
}
