package com.mgs.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ReportRun.
 */
@Entity
@Table(name = "report_run")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ReportRun extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 255)
    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "filter_json")
    private String filterJson;

    @Size(max = 10)
    @Column(name = "format", length = 10)
    private String format;

    @Column(name = "file_path")
    private String filePath;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tenantProfile", "tenantBranding" }, allowSetters = true)
    private Tenant tenant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private ReportTemplate template;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "tenant" }, allowSetters = true)
    private User generatedByUser;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ReportRun id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public ReportRun name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilterJson() {
        return this.filterJson;
    }

    public ReportRun filterJson(String filterJson) {
        this.setFilterJson(filterJson);
        return this;
    }

    public void setFilterJson(String filterJson) {
        this.filterJson = filterJson;
    }

    public String getFormat() {
        return this.format;
    }

    public ReportRun format(String format) {
        this.setFormat(format);
        return this;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public ReportRun filePath(String filePath) {
        this.setFilePath(filePath);
        return this;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Tenant getTenant() {
        return this.tenant;
    }

    public void setTenant(Tenant tenant) {
        this.tenant = tenant;
    }

    public ReportRun tenant(Tenant tenant) {
        this.setTenant(tenant);
        return this;
    }

    public ReportTemplate getTemplate() {
        return this.template;
    }

    public void setTemplate(ReportTemplate reportTemplate) {
        this.template = reportTemplate;
    }

    public ReportRun template(ReportTemplate reportTemplate) {
        this.setTemplate(reportTemplate);
        return this;
    }

    public User getGeneratedByUser() {
        return this.generatedByUser;
    }

    public void setGeneratedByUser(User user) {
        this.generatedByUser = user;
    }

    public ReportRun generatedByUser(User user) {
        this.setGeneratedByUser(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ReportRun)) {
            return false;
        }
        return getId() != null && getId().equals(((ReportRun) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ReportRun{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", filterJson='" + getFilterJson() + "'" +
            ", format='" + getFormat() + "'" +
            ", filePath='" + getFilePath() + "'" +
            "}";
    }
}
