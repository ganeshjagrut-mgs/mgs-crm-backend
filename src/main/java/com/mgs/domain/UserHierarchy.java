package com.mgs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserHierarchy.
 */
@Entity
@Table(name = "user_hierarchy")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserHierarchy extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 30)
    @Column(name = "relationship_type", length = 30)
    private String relationshipType;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tenantProfile", "tenantBranding" }, allowSetters = true)
    private Tenant tenant;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private User parentUser;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private User childUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserHierarchy id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRelationshipType() {
        return this.relationshipType;
    }

    public UserHierarchy relationshipType(String relationshipType) {
        this.setRelationshipType(relationshipType);
        return this;
    }

    public void setRelationshipType(String relationshipType) {
        this.relationshipType = relationshipType;
    }

    public Tenant getTenant() {
        return this.tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public UserHierarchy tenant(Tenant tenant) {
        this.setTenant(tenant);
        return this;
    }

    public User getParentUser() {
        return this.parentUser;
    }

    public void setParentUser(User user) {
        this.parentUser = user;
    }

    public UserHierarchy parentUser(User user) {
        this.setParentUser(user);
        return this;
    }

    public User getChildUser() {
        return this.childUser;
    }

    public void setChildUser(User user) {
        this.childUser = user;
    }

    public UserHierarchy childUser(User user) {
        this.setChildUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserHierarchy)) {
            return false;
        }
        return getId() != null && getId().equals(((UserHierarchy) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserHierarchy{" +
            "id=" + getId() +
            ", relationshipType='" + getRelationshipType() + "'" +
            "}";
    }
}
