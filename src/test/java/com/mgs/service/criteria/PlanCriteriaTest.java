package com.mgs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PlanCriteriaTest {

    @Test
    void newPlanCriteriaHasAllFiltersNullTest() {
        var planCriteria = new PlanCriteria();
        assertThat(planCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void planCriteriaFluentMethodsCreatesFiltersTest() {
        var planCriteria = new PlanCriteria();

        setAllFilters(planCriteria);

        assertThat(planCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void planCriteriaCopyCreatesNullFilterTest() {
        var planCriteria = new PlanCriteria();
        var copy = planCriteria.copy();

        assertThat(planCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(planCriteria)
        );
    }

    @Test
    void planCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var planCriteria = new PlanCriteria();
        setAllFilters(planCriteria);

        var copy = planCriteria.copy();

        assertThat(planCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(planCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var planCriteria = new PlanCriteria();

        assertThat(planCriteria).hasToString("PlanCriteria{}");
    }

    private static void setAllFilters(PlanCriteria planCriteria) {
        planCriteria.id();
        planCriteria.code();
        planCriteria.name();
        planCriteria.description();
        planCriteria.maxUsers();
        planCriteria.maxStorageMb();
        planCriteria.maxCustomers();
        planCriteria.maxContacts();
        planCriteria.maxQuotations();
        planCriteria.maxComplaints();
        planCriteria.pricePerMonth();
        planCriteria.currency();
        planCriteria.isActive();
        planCriteria.distinct();
    }

    private static Condition<PlanCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getCode()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getMaxUsers()) &&
                condition.apply(criteria.getMaxStorageMb()) &&
                condition.apply(criteria.getMaxCustomers()) &&
                condition.apply(criteria.getMaxContacts()) &&
                condition.apply(criteria.getMaxQuotations()) &&
                condition.apply(criteria.getMaxComplaints()) &&
                condition.apply(criteria.getPricePerMonth()) &&
                condition.apply(criteria.getCurrency()) &&
                condition.apply(criteria.getIsActive()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PlanCriteria> copyFiltersAre(PlanCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getCode(), copy.getCode()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getMaxUsers(), copy.getMaxUsers()) &&
                condition.apply(criteria.getMaxStorageMb(), copy.getMaxStorageMb()) &&
                condition.apply(criteria.getMaxCustomers(), copy.getMaxCustomers()) &&
                condition.apply(criteria.getMaxContacts(), copy.getMaxContacts()) &&
                condition.apply(criteria.getMaxQuotations(), copy.getMaxQuotations()) &&
                condition.apply(criteria.getMaxComplaints(), copy.getMaxComplaints()) &&
                condition.apply(criteria.getPricePerMonth(), copy.getPricePerMonth()) &&
                condition.apply(criteria.getCurrency(), copy.getCurrency()) &&
                condition.apply(criteria.getIsActive(), copy.getIsActive()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
