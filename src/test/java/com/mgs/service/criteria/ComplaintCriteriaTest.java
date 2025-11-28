package com.mgs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ComplaintCriteriaTest {

    @Test
    void newComplaintCriteriaHasAllFiltersNullTest() {
        var complaintCriteria = new ComplaintCriteria();
        assertThat(complaintCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void complaintCriteriaFluentMethodsCreatesFiltersTest() {
        var complaintCriteria = new ComplaintCriteria();

        setAllFilters(complaintCriteria);

        assertThat(complaintCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void complaintCriteriaCopyCreatesNullFilterTest() {
        var complaintCriteria = new ComplaintCriteria();
        var copy = complaintCriteria.copy();

        assertThat(complaintCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(complaintCriteria)
        );
    }

    @Test
    void complaintCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var complaintCriteria = new ComplaintCriteria();
        setAllFilters(complaintCriteria);

        var copy = complaintCriteria.copy();

        assertThat(complaintCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(complaintCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var complaintCriteria = new ComplaintCriteria();

        assertThat(complaintCriteria).hasToString("ComplaintCriteria{}");
    }

    private static void setAllFilters(ComplaintCriteria complaintCriteria) {
        complaintCriteria.id();
        complaintCriteria.complaintNumber();
        complaintCriteria.subject();
        complaintCriteria.description();
        complaintCriteria.priority();
        complaintCriteria.status();
        complaintCriteria.source();
        complaintCriteria.tenantId();
        complaintCriteria.customerId();
        complaintCriteria.contactId();
        complaintCriteria.categoryId();
        complaintCriteria.pipelineId();
        complaintCriteria.stageId();
        complaintCriteria.assignedDepartmentId();
        complaintCriteria.assignedUserId();
        complaintCriteria.distinct();
    }

    private static Condition<ComplaintCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getComplaintNumber()) &&
                condition.apply(criteria.getSubject()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getPriority()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getSource()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getCustomerId()) &&
                condition.apply(criteria.getContactId()) &&
                condition.apply(criteria.getCategoryId()) &&
                condition.apply(criteria.getPipelineId()) &&
                condition.apply(criteria.getStageId()) &&
                condition.apply(criteria.getAssignedDepartmentId()) &&
                condition.apply(criteria.getAssignedUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ComplaintCriteria> copyFiltersAre(ComplaintCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getComplaintNumber(), copy.getComplaintNumber()) &&
                condition.apply(criteria.getSubject(), copy.getSubject()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getPriority(), copy.getPriority()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getSource(), copy.getSource()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getCustomerId(), copy.getCustomerId()) &&
                condition.apply(criteria.getContactId(), copy.getContactId()) &&
                condition.apply(criteria.getCategoryId(), copy.getCategoryId()) &&
                condition.apply(criteria.getPipelineId(), copy.getPipelineId()) &&
                condition.apply(criteria.getStageId(), copy.getStageId()) &&
                condition.apply(criteria.getAssignedDepartmentId(), copy.getAssignedDepartmentId()) &&
                condition.apply(criteria.getAssignedUserId(), copy.getAssignedUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
