package com.crm.domain;

import com.crm.domain.enumeration.MasterStaticGroupEditable;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * MasterStaticGroup - Groups master static data
 */
@Entity
@Table(name = "master_static_group")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class MasterStaticGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "ui_label")
    private String uiLabel;

    @Enumerated(EnumType.STRING)
    @Column(name = "editable")
    private MasterStaticGroupEditable editable;

    @ManyToOne(fetch = FetchType.LAZY)
    private EntityType entityType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public MasterStaticGroup id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public MasterStaticGroup name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public MasterStaticGroup description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUiLabel() {
        return this.uiLabel;
    }

    public MasterStaticGroup uiLabel(String uiLabel) {
        this.setUiLabel(uiLabel);
        return this;
    }

    public void setUiLabel(String uiLabel) {
        this.uiLabel = uiLabel;
    }

    public MasterStaticGroupEditable getEditable() {
        return this.editable;
    }

    public MasterStaticGroup editable(MasterStaticGroupEditable editable) {
        this.setEditable(editable);
        return this;
    }

    public void setEditable(MasterStaticGroupEditable editable) {
        this.editable = editable;
    }

    public EntityType getEntityType() {
        return this.entityType;
    }

    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    public MasterStaticGroup entityType(EntityType entityType) {
        this.setEntityType(entityType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MasterStaticGroup)) {
            return false;
        }
        return getId() != null && getId().equals(((MasterStaticGroup) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "MasterStaticGroup{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", uiLabel='" + getUiLabel() + "'" +
            ", editable='" + getEditable() + "'" +
            "}";
    }
}
