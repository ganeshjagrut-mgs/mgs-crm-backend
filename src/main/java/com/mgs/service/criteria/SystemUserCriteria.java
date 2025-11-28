package com.mgs.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.mgs.domain.SystemUser} entity. This class is used
 * in {@link com.mgs.web.rest.SystemUserResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /system-users?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SystemUserCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter email;

    private StringFilter passwordHash;

    private BooleanFilter isSuperAdmin;

    private Boolean distinct;

    public SystemUserCriteria() {}

    public SystemUserCriteria(SystemUserCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.email = other.optionalEmail().map(StringFilter::copy).orElse(null);
        this.passwordHash = other.optionalPasswordHash().map(StringFilter::copy).orElse(null);
        this.isSuperAdmin = other.optionalIsSuperAdmin().map(BooleanFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public SystemUserCriteria copy() {
        return new SystemUserCriteria(this);
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

    public StringFilter getEmail() {
        return email;
    }

    public Optional<StringFilter> optionalEmail() {
        return Optional.ofNullable(email);
    }

    public StringFilter email() {
        if (email == null) {
            setEmail(new StringFilter());
        }
        return email;
    }

    public void setEmail(StringFilter email) {
        this.email = email;
    }

    public StringFilter getPasswordHash() {
        return passwordHash;
    }

    public Optional<StringFilter> optionalPasswordHash() {
        return Optional.ofNullable(passwordHash);
    }

    public StringFilter passwordHash() {
        if (passwordHash == null) {
            setPasswordHash(new StringFilter());
        }
        return passwordHash;
    }

    public void setPasswordHash(StringFilter passwordHash) {
        this.passwordHash = passwordHash;
    }

    public BooleanFilter getIsSuperAdmin() {
        return isSuperAdmin;
    }

    public Optional<BooleanFilter> optionalIsSuperAdmin() {
        return Optional.ofNullable(isSuperAdmin);
    }

    public BooleanFilter isSuperAdmin() {
        if (isSuperAdmin == null) {
            setIsSuperAdmin(new BooleanFilter());
        }
        return isSuperAdmin;
    }

    public void setIsSuperAdmin(BooleanFilter isSuperAdmin) {
        this.isSuperAdmin = isSuperAdmin;
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
        final SystemUserCriteria that = (SystemUserCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(email, that.email) &&
            Objects.equals(passwordHash, that.passwordHash) &&
            Objects.equals(isSuperAdmin, that.isSuperAdmin) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, email, passwordHash, isSuperAdmin, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SystemUserCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalEmail().map(f -> "email=" + f + ", ").orElse("") +
            optionalPasswordHash().map(f -> "passwordHash=" + f + ", ").orElse("") +
            optionalIsSuperAdmin().map(f -> "isSuperAdmin=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
