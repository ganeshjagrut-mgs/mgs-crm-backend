package com.mgs.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mgs.domain.Country} entity. This class is used
 * in {@link com.mgs.web.rest.CountryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /countries?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CountryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter isoCode2;

    private StringFilter isoCode3;

    private StringFilter name;

    private Boolean distinct;

    public CountryCriteria() {}

    public CountryCriteria(CountryCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.isoCode2 = other.optionalIsoCode2().map(StringFilter::copy).orElse(null);
        this.isoCode3 = other.optionalIsoCode3().map(StringFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public CountryCriteria copy() {
        return new CountryCriteria(this);
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

    public StringFilter getIsoCode2() {
        return isoCode2;
    }

    public Optional<StringFilter> optionalIsoCode2() {
        return Optional.ofNullable(isoCode2);
    }

    public StringFilter isoCode2() {
        if (isoCode2 == null) {
            setIsoCode2(new StringFilter());
        }
        return isoCode2;
    }

    public void setIsoCode2(StringFilter isoCode2) {
        this.isoCode2 = isoCode2;
    }

    public StringFilter getIsoCode3() {
        return isoCode3;
    }

    public Optional<StringFilter> optionalIsoCode3() {
        return Optional.ofNullable(isoCode3);
    }

    public StringFilter isoCode3() {
        if (isoCode3 == null) {
            setIsoCode3(new StringFilter());
        }
        return isoCode3;
    }

    public void setIsoCode3(StringFilter isoCode3) {
        this.isoCode3 = isoCode3;
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
        final CountryCriteria that = (CountryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(isoCode2, that.isoCode2) &&
            Objects.equals(isoCode3, that.isoCode3) &&
            Objects.equals(name, that.name) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, isoCode2, isoCode3, name, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CountryCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalIsoCode2().map(f -> "isoCode2=" + f + ", ").orElse("") +
            optionalIsoCode3().map(f -> "isoCode3=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
