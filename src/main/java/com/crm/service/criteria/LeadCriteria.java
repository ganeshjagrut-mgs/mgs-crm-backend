package com.crm.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.crm.domain.Lead} entity. This class is used
 * in {@link com.crm.web.rest.LeadResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /leads?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LeadCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter leadNumber;

    private IntegerFilter annualRevenue;

    private LongFilter userId;

    private LongFilter customerId;

    private LongFilter leadSourceId;

    private LongFilter industryTypeId;

    private LongFilter leadStatusId;

    private Boolean distinct;

    public LeadCriteria() {}

    public LeadCriteria(LeadCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.leadNumber = other.leadNumber == null ? null : other.leadNumber.copy();
        this.annualRevenue = other.annualRevenue == null ? null : other.annualRevenue.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.customerId = other.customerId == null ? null : other.customerId.copy();
        this.leadSourceId = other.leadSourceId == null ? null : other.leadSourceId.copy();
        this.industryTypeId = other.industryTypeId == null ? null : other.industryTypeId.copy();
        this.leadStatusId = other.leadStatusId == null ? null : other.leadStatusId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public LeadCriteria copy() {
        return new LeadCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public LongFilter id() {
        if (id == null) {
            id = new LongFilter();
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public StringFilter name() {
        if (name == null) {
            name = new StringFilter();
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getLeadNumber() {
        return leadNumber;
    }

    public StringFilter leadNumber() {
        if (leadNumber == null) {
            leadNumber = new StringFilter();
        }
        return leadNumber;
    }

    public void setLeadNumber(StringFilter leadNumber) {
        this.leadNumber = leadNumber;
    }

    public IntegerFilter getAnnualRevenue() {
        return annualRevenue;
    }

    public IntegerFilter annualRevenue() {
        if (annualRevenue == null) {
            annualRevenue = new IntegerFilter();
        }
        return annualRevenue;
    }

    public void setAnnualRevenue(IntegerFilter annualRevenue) {
        this.annualRevenue = annualRevenue;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public LongFilter userId() {
        if (userId == null) {
            userId = new LongFilter();
        }
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public LongFilter customerId() {
        if (customerId == null) {
            customerId = new LongFilter();
        }
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public LongFilter getLeadSourceId() {
        return leadSourceId;
    }

    public LongFilter leadSourceId() {
        if (leadSourceId == null) {
            leadSourceId = new LongFilter();
        }
        return leadSourceId;
    }

    public void setLeadSourceId(LongFilter leadSourceId) {
        this.leadSourceId = leadSourceId;
    }

    public LongFilter getIndustryTypeId() {
        return industryTypeId;
    }

    public LongFilter industryTypeId() {
        if (industryTypeId == null) {
            industryTypeId = new LongFilter();
        }
        return industryTypeId;
    }

    public void setIndustryTypeId(LongFilter industryTypeId) {
        this.industryTypeId = industryTypeId;
    }

    public LongFilter getLeadStatusId() {
        return leadStatusId;
    }

    public LongFilter leadStatusId() {
        if (leadStatusId == null) {
            leadStatusId = new LongFilter();
        }
        return leadStatusId;
    }

    public void setLeadStatusId(LongFilter leadStatusId) {
        this.leadStatusId = leadStatusId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final LeadCriteria that = (LeadCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(leadNumber, that.leadNumber) &&
            Objects.equals(annualRevenue, that.annualRevenue) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(leadSourceId, that.leadSourceId) &&
            Objects.equals(industryTypeId, that.industryTypeId) &&
            Objects.equals(leadStatusId, that.leadStatusId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, leadNumber, annualRevenue, userId, customerId, leadSourceId, industryTypeId, leadStatusId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LeadCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (name != null ? "name=" + name + ", " : "") +
            (leadNumber != null ? "leadNumber=" + leadNumber + ", " : "") +
            (annualRevenue != null ? "annualRevenue=" + annualRevenue + ", " : "") +
            (userId != null ? "userId=" + userId + ", " : "") +
            (customerId != null ? "customerId=" + customerId + ", " : "") +
            (leadSourceId != null ? "leadSourceId=" + leadSourceId + ", " : "") +
            (industryTypeId != null ? "industryTypeId=" + industryTypeId + ", " : "") +
            (leadStatusId != null ? "leadStatusId=" + leadStatusId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
