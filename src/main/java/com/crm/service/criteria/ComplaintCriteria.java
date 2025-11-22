package com.crm.service.criteria;

import com.crm.domain.enumeration.ComplaintStatus;
import java.io.Serializable;
import java.util.Objects;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.crm.domain.Complaint} entity. This class is used
 * in {@link com.crm.web.rest.ComplaintResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /complaints?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ComplaintCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ComplaintStatus
     */
    public static class ComplaintStatusFilter extends Filter<ComplaintStatus> {

        public ComplaintStatusFilter() {}

        public ComplaintStatusFilter(ComplaintStatusFilter filter) {
            super(filter);
        }

        @Override
        public ComplaintStatusFilter copy() {
            return new ComplaintStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter complaintNumber;

    private InstantFilter complaintDate;

    private StringFilter recordNumbers;

    private StringFilter customerContactNumber;

    private StringFilter customerContactEmail;

    private InstantFilter expectedClosureDate;

    private ComplaintStatusFilter complaintStatus;

    private InstantFilter complaintClosureDate;

    private LongFilter customerNameId;

    private LongFilter complaintRelatedToId;

    private LongFilter typeOfComplaintId;

    private LongFilter complaintRelatedPersonsId;

    private Boolean distinct;

    public ComplaintCriteria() {}

    public ComplaintCriteria(ComplaintCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.complaintNumber = other.complaintNumber == null ? null : other.complaintNumber.copy();
        this.complaintDate = other.complaintDate == null ? null : other.complaintDate.copy();
        this.recordNumbers = other.recordNumbers == null ? null : other.recordNumbers.copy();
        this.customerContactNumber = other.customerContactNumber == null ? null : other.customerContactNumber.copy();
        this.customerContactEmail = other.customerContactEmail == null ? null : other.customerContactEmail.copy();
        this.expectedClosureDate = other.expectedClosureDate == null ? null : other.expectedClosureDate.copy();
        this.complaintStatus = other.complaintStatus == null ? null : other.complaintStatus.copy();
        this.complaintClosureDate = other.complaintClosureDate == null ? null : other.complaintClosureDate.copy();
        this.customerNameId = other.customerNameId == null ? null : other.customerNameId.copy();
        this.complaintRelatedToId = other.complaintRelatedToId == null ? null : other.complaintRelatedToId.copy();
        this.typeOfComplaintId = other.typeOfComplaintId == null ? null : other.typeOfComplaintId.copy();
        this.complaintRelatedPersonsId = other.complaintRelatedPersonsId == null ? null : other.complaintRelatedPersonsId.copy();
        this.distinct = other.distinct;
    }

    @Override
    public ComplaintCriteria copy() {
        return new ComplaintCriteria(this);
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

    public StringFilter getComplaintNumber() {
        return complaintNumber;
    }

    public StringFilter complaintNumber() {
        if (complaintNumber == null) {
            complaintNumber = new StringFilter();
        }
        return complaintNumber;
    }

    public void setComplaintNumber(StringFilter complaintNumber) {
        this.complaintNumber = complaintNumber;
    }

    public InstantFilter getComplaintDate() {
        return complaintDate;
    }

    public InstantFilter complaintDate() {
        if (complaintDate == null) {
            complaintDate = new InstantFilter();
        }
        return complaintDate;
    }

    public void setComplaintDate(InstantFilter complaintDate) {
        this.complaintDate = complaintDate;
    }

    public StringFilter getRecordNumbers() {
        return recordNumbers;
    }

    public StringFilter recordNumbers() {
        if (recordNumbers == null) {
            recordNumbers = new StringFilter();
        }
        return recordNumbers;
    }

    public void setRecordNumbers(StringFilter recordNumbers) {
        this.recordNumbers = recordNumbers;
    }

    public StringFilter getCustomerContactNumber() {
        return customerContactNumber;
    }

    public StringFilter customerContactNumber() {
        if (customerContactNumber == null) {
            customerContactNumber = new StringFilter();
        }
        return customerContactNumber;
    }

    public void setCustomerContactNumber(StringFilter customerContactNumber) {
        this.customerContactNumber = customerContactNumber;
    }

    public StringFilter getCustomerContactEmail() {
        return customerContactEmail;
    }

    public StringFilter customerContactEmail() {
        if (customerContactEmail == null) {
            customerContactEmail = new StringFilter();
        }
        return customerContactEmail;
    }

    public void setCustomerContactEmail(StringFilter customerContactEmail) {
        this.customerContactEmail = customerContactEmail;
    }

    public InstantFilter getExpectedClosureDate() {
        return expectedClosureDate;
    }

    public InstantFilter expectedClosureDate() {
        if (expectedClosureDate == null) {
            expectedClosureDate = new InstantFilter();
        }
        return expectedClosureDate;
    }

    public void setExpectedClosureDate(InstantFilter expectedClosureDate) {
        this.expectedClosureDate = expectedClosureDate;
    }

    public ComplaintStatusFilter getComplaintStatus() {
        return complaintStatus;
    }

    public ComplaintStatusFilter complaintStatus() {
        if (complaintStatus == null) {
            complaintStatus = new ComplaintStatusFilter();
        }
        return complaintStatus;
    }

    public void setComplaintStatus(ComplaintStatusFilter complaintStatus) {
        this.complaintStatus = complaintStatus;
    }

    public InstantFilter getComplaintClosureDate() {
        return complaintClosureDate;
    }

    public InstantFilter complaintClosureDate() {
        if (complaintClosureDate == null) {
            complaintClosureDate = new InstantFilter();
        }
        return complaintClosureDate;
    }

    public void setComplaintClosureDate(InstantFilter complaintClosureDate) {
        this.complaintClosureDate = complaintClosureDate;
    }

    public LongFilter getCustomerNameId() {
        return customerNameId;
    }

    public LongFilter customerNameId() {
        if (customerNameId == null) {
            customerNameId = new LongFilter();
        }
        return customerNameId;
    }

    public void setCustomerNameId(LongFilter customerNameId) {
        this.customerNameId = customerNameId;
    }

    public LongFilter getComplaintRelatedToId() {
        return complaintRelatedToId;
    }

    public LongFilter complaintRelatedToId() {
        if (complaintRelatedToId == null) {
            complaintRelatedToId = new LongFilter();
        }
        return complaintRelatedToId;
    }

    public void setComplaintRelatedToId(LongFilter complaintRelatedToId) {
        this.complaintRelatedToId = complaintRelatedToId;
    }

    public LongFilter getTypeOfComplaintId() {
        return typeOfComplaintId;
    }

    public LongFilter typeOfComplaintId() {
        if (typeOfComplaintId == null) {
            typeOfComplaintId = new LongFilter();
        }
        return typeOfComplaintId;
    }

    public void setTypeOfComplaintId(LongFilter typeOfComplaintId) {
        this.typeOfComplaintId = typeOfComplaintId;
    }

    public LongFilter getComplaintRelatedPersonsId() {
        return complaintRelatedPersonsId;
    }

    public LongFilter complaintRelatedPersonsId() {
        if (complaintRelatedPersonsId == null) {
            complaintRelatedPersonsId = new LongFilter();
        }
        return complaintRelatedPersonsId;
    }

    public void setComplaintRelatedPersonsId(LongFilter complaintRelatedPersonsId) {
        this.complaintRelatedPersonsId = complaintRelatedPersonsId;
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
        final ComplaintCriteria that = (ComplaintCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(complaintNumber, that.complaintNumber) &&
            Objects.equals(complaintDate, that.complaintDate) &&
            Objects.equals(recordNumbers, that.recordNumbers) &&
            Objects.equals(customerContactNumber, that.customerContactNumber) &&
            Objects.equals(customerContactEmail, that.customerContactEmail) &&
            Objects.equals(expectedClosureDate, that.expectedClosureDate) &&
            Objects.equals(complaintStatus, that.complaintStatus) &&
            Objects.equals(complaintClosureDate, that.complaintClosureDate) &&
            Objects.equals(customerNameId, that.customerNameId) &&
            Objects.equals(complaintRelatedToId, that.complaintRelatedToId) &&
            Objects.equals(typeOfComplaintId, that.typeOfComplaintId) &&
            Objects.equals(complaintRelatedPersonsId, that.complaintRelatedPersonsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            complaintNumber,
            complaintDate,
            recordNumbers,
            customerContactNumber,
            customerContactEmail,
            expectedClosureDate,
            complaintStatus,
            complaintClosureDate,
            customerNameId,
            complaintRelatedToId,
            typeOfComplaintId,
            complaintRelatedPersonsId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ComplaintCriteria{" +
            (id != null ? "id=" + id + ", " : "") +
            (complaintNumber != null ? "complaintNumber=" + complaintNumber + ", " : "") +
            (complaintDate != null ? "complaintDate=" + complaintDate + ", " : "") +
            (recordNumbers != null ? "recordNumbers=" + recordNumbers + ", " : "") +
            (customerContactNumber != null ? "customerContactNumber=" + customerContactNumber + ", " : "") +
            (customerContactEmail != null ? "customerContactEmail=" + customerContactEmail + ", " : "") +
            (expectedClosureDate != null ? "expectedClosureDate=" + expectedClosureDate + ", " : "") +
            (complaintStatus != null ? "complaintStatus=" + complaintStatus + ", " : "") +
            (complaintClosureDate != null ? "complaintClosureDate=" + complaintClosureDate + ", " : "") +
            (customerNameId != null ? "customerNameId=" + customerNameId + ", " : "") +
            (complaintRelatedToId != null ? "complaintRelatedToId=" + complaintRelatedToId + ", " : "") +
            (typeOfComplaintId != null ? "typeOfComplaintId=" + typeOfComplaintId + ", " : "") +
            (complaintRelatedPersonsId != null ? "complaintRelatedPersonsId=" + complaintRelatedPersonsId + ", " : "") +
            (distinct != null ? "distinct=" + distinct + ", " : "") +
            "}";
    }
}
