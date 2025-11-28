package com.mgs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TicketCriteriaTest {

    @Test
    void newTicketCriteriaHasAllFiltersNullTest() {
        var ticketCriteria = new TicketCriteria();
        assertThat(ticketCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void ticketCriteriaFluentMethodsCreatesFiltersTest() {
        var ticketCriteria = new TicketCriteria();

        setAllFilters(ticketCriteria);

        assertThat(ticketCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void ticketCriteriaCopyCreatesNullFilterTest() {
        var ticketCriteria = new TicketCriteria();
        var copy = ticketCriteria.copy();

        assertThat(ticketCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(ticketCriteria)
        );
    }

    @Test
    void ticketCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var ticketCriteria = new TicketCriteria();
        setAllFilters(ticketCriteria);

        var copy = ticketCriteria.copy();

        assertThat(ticketCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(ticketCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var ticketCriteria = new TicketCriteria();

        assertThat(ticketCriteria).hasToString("TicketCriteria{}");
    }

    private static void setAllFilters(TicketCriteria ticketCriteria) {
        ticketCriteria.id();
        ticketCriteria.ticketNumber();
        ticketCriteria.subject();
        ticketCriteria.description();
        ticketCriteria.priority();
        ticketCriteria.status();
        ticketCriteria.tenantId();
        ticketCriteria.customerId();
        ticketCriteria.contactId();
        ticketCriteria.assignedToId();
        ticketCriteria.distinct();
    }

    private static Condition<TicketCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getTicketNumber()) &&
                condition.apply(criteria.getSubject()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getPriority()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getCustomerId()) &&
                condition.apply(criteria.getContactId()) &&
                condition.apply(criteria.getAssignedToId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TicketCriteria> copyFiltersAre(TicketCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getTicketNumber(), copy.getTicketNumber()) &&
                condition.apply(criteria.getSubject(), copy.getSubject()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getPriority(), copy.getPriority()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getCustomerId(), copy.getCustomerId()) &&
                condition.apply(criteria.getContactId(), copy.getContactId()) &&
                condition.apply(criteria.getAssignedToId(), copy.getAssignedToId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
