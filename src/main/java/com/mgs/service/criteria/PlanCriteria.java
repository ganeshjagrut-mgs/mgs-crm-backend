package com.mgs.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mgs.domain.Plan} entity. This class is used
 * in {@link com.mgs.web.rest.PlanResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /plans?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PlanCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter code;

    private StringFilter name;

    private StringFilter description;

    private IntegerFilter maxUsers;

    private IntegerFilter maxStorageMb;

    private IntegerFilter maxCustomers;

    private IntegerFilter maxContacts;

    private IntegerFilter maxQuotations;

    private IntegerFilter maxComplaints;

    private BigDecimalFilter pricePerMonth;

    private StringFilter currency;

    private BooleanFilter isActive;

    private Boolean distinct;

    public PlanCriteria() {}

    public PlanCriteria(PlanCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.code = other.optionalCode().map(StringFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.maxUsers = other.optionalMaxUsers().map(IntegerFilter::copy).orElse(null);
        this.maxStorageMb = other.optionalMaxStorageMb().map(IntegerFilter::copy).orElse(null);
        this.maxCustomers = other.optionalMaxCustomers().map(IntegerFilter::copy).orElse(null);
        this.maxContacts = other.optionalMaxContacts().map(IntegerFilter::copy).orElse(null);
        this.maxQuotations = other.optionalMaxQuotations().map(IntegerFilter::copy).orElse(null);
        this.maxComplaints = other.optionalMaxComplaints().map(IntegerFilter::copy).orElse(null);
        this.pricePerMonth = other.optionalPricePerMonth().map(BigDecimalFilter::copy).orElse(null);
        this.currency = other.optionalCurrency().map(StringFilter::copy).orElse(null);
        this.isActive = other.optionalIsActive().map(BooleanFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PlanCriteria copy() {
        return new PlanCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getCode() {
        return code;
    }

    public Optional<StringFilter> optionalCode() {
        return Optional.ofNullable(code);
    }

    public StringFilter code() {
        if (code == null) {
            setCode(new StringFilter());
        }
        return code;
    }

    public void setCode(StringFilter code) {
        this.code = code;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public IntegerFilter getMaxUsers() {
        return maxUsers;
    }

    public Optional<IntegerFilter> optionalMaxUsers() {
        return Optional.ofNullable(maxUsers);
    }

    public IntegerFilter maxUsers() {
        if (maxUsers == null) {
            setMaxUsers(new IntegerFilter());
        }
        return maxUsers;
    }

    public void setMaxUsers(IntegerFilter maxUsers) {
        this.maxUsers = maxUsers;
    }

    public IntegerFilter getMaxStorageMb() {
        return maxStorageMb;
    }

    public Optional<IntegerFilter> optionalMaxStorageMb() {
        return Optional.ofNullable(maxStorageMb);
    }

    public IntegerFilter maxStorageMb() {
        if (maxStorageMb == null) {
            setMaxStorageMb(new IntegerFilter());
        }
        return maxStorageMb;
    }

    public void setMaxStorageMb(IntegerFilter maxStorageMb) {
        this.maxStorageMb = maxStorageMb;
    }

    public IntegerFilter getMaxCustomers() {
        return maxCustomers;
    }

    public Optional<IntegerFilter> optionalMaxCustomers() {
        return Optional.ofNullable(maxCustomers);
    }

    public IntegerFilter maxCustomers() {
        if (maxCustomers == null) {
            setMaxCustomers(new IntegerFilter());
        }
        return maxCustomers;
    }

    public void setMaxCustomers(IntegerFilter maxCustomers) {
        this.maxCustomers = maxCustomers;
    }

    public IntegerFilter getMaxContacts() {
        return maxContacts;
    }

    public Optional<IntegerFilter> optionalMaxContacts() {
        return Optional.ofNullable(maxContacts);
    }

    public IntegerFilter maxContacts() {
        if (maxContacts == null) {
            setMaxContacts(new IntegerFilter());
        }
        return maxContacts;
    }

    public void setMaxContacts(IntegerFilter maxContacts) {
        this.maxContacts = maxContacts;
    }

    public IntegerFilter getMaxQuotations() {
        return maxQuotations;
    }

    public Optional<IntegerFilter> optionalMaxQuotations() {
        return Optional.ofNullable(maxQuotations);
    }

    public IntegerFilter maxQuotations() {
        if (maxQuotations == null) {
            setMaxQuotations(new IntegerFilter());
        }
        return maxQuotations;
    }

    public void setMaxQuotations(IntegerFilter maxQuotations) {
        this.maxQuotations = maxQuotations;
    }

    public IntegerFilter getMaxComplaints() {
        return maxComplaints;
    }

    public Optional<IntegerFilter> optionalMaxComplaints() {
        return Optional.ofNullable(maxComplaints);
    }

    public IntegerFilter maxComplaints() {
        if (maxComplaints == null) {
            setMaxComplaints(new IntegerFilter());
        }
        return maxComplaints;
    }

    public void setMaxComplaints(IntegerFilter maxComplaints) {
        this.maxComplaints = maxComplaints;
    }

    public BigDecimalFilter getPricePerMonth() {
        return pricePerMonth;
    }

    public Optional<BigDecimalFilter> optionalPricePerMonth() {
        return Optional.ofNullable(pricePerMonth);
    }

    public BigDecimalFilter pricePerMonth() {
        if (pricePerMonth == null) {
            setPricePerMonth(new BigDecimalFilter());
        }
        return pricePerMonth;
    }

    public void setPricePerMonth(BigDecimalFilter pricePerMonth) {
        this.pricePerMonth = pricePerMonth;
    }

    public StringFilter getCurrency() {
        return currency;
    }

    public Optional<StringFilter> optionalCurrency() {
        return Optional.ofNullable(currency);
    }

    public StringFilter currency() {
        if (currency == null) {
            setCurrency(new StringFilter());
        }
        return currency;
    }

    public void setCurrency(StringFilter currency) {
        this.currency = currency;
    }

    public BooleanFilter getIsActive() {
        return isActive;
    }

    public Optional<BooleanFilter> optionalIsActive() {
        return Optional.ofNullable(isActive);
    }

    public BooleanFilter isActive() {
        if (isActive == null) {
            setIsActive(new BooleanFilter());
        }
        return isActive;
    }

    public void setIsActive(BooleanFilter isActive) {
        this.isActive = isActive;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
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
        final PlanCriteria that = (PlanCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(code, that.code) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(maxUsers, that.maxUsers) &&
            Objects.equals(maxStorageMb, that.maxStorageMb) &&
            Objects.equals(maxCustomers, that.maxCustomers) &&
            Objects.equals(maxContacts, that.maxContacts) &&
            Objects.equals(maxQuotations, that.maxQuotations) &&
            Objects.equals(maxComplaints, that.maxComplaints) &&
            Objects.equals(pricePerMonth, that.pricePerMonth) &&
            Objects.equals(currency, that.currency) &&
            Objects.equals(isActive, that.isActive) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            code,
            name,
            description,
            maxUsers,
            maxStorageMb,
            maxCustomers,
            maxContacts,
            maxQuotations,
            maxComplaints,
            pricePerMonth,
            currency,
            isActive,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PlanCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalCode().map(f -> "code=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalMaxUsers().map(f -> "maxUsers=" + f + ", ").orElse("") +
            optionalMaxStorageMb().map(f -> "maxStorageMb=" + f + ", ").orElse("") +
            optionalMaxCustomers().map(f -> "maxCustomers=" + f + ", ").orElse("") +
            optionalMaxContacts().map(f -> "maxContacts=" + f + ", ").orElse("") +
            optionalMaxQuotations().map(f -> "maxQuotations=" + f + ", ").orElse("") +
            optionalMaxComplaints().map(f -> "maxComplaints=" + f + ", ").orElse("") +
            optionalPricePerMonth().map(f -> "pricePerMonth=" + f + ", ").orElse("") +
            optionalCurrency().map(f -> "currency=" + f + ", ").orElse("") +
            optionalIsActive().map(f -> "isActive=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
