package com.mgs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PipelineCriteriaTest {

    @Test
    void newPipelineCriteriaHasAllFiltersNullTest() {
        var pipelineCriteria = new PipelineCriteria();
        assertThat(pipelineCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void pipelineCriteriaFluentMethodsCreatesFiltersTest() {
        var pipelineCriteria = new PipelineCriteria();

        setAllFilters(pipelineCriteria);

        assertThat(pipelineCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void pipelineCriteriaCopyCreatesNullFilterTest() {
        var pipelineCriteria = new PipelineCriteria();
        var copy = pipelineCriteria.copy();

        assertThat(pipelineCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(pipelineCriteria)
        );
    }

    @Test
    void pipelineCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var pipelineCriteria = new PipelineCriteria();
        setAllFilters(pipelineCriteria);

        var copy = pipelineCriteria.copy();

        assertThat(pipelineCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(pipelineCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var pipelineCriteria = new PipelineCriteria();

        assertThat(pipelineCriteria).hasToString("PipelineCriteria{}");
    }

    private static void setAllFilters(PipelineCriteria pipelineCriteria) {
        pipelineCriteria.id();
        pipelineCriteria.name();
        pipelineCriteria.nameSearch();
        pipelineCriteria.module();
        pipelineCriteria.isDefault();
        pipelineCriteria.tenantId();
        pipelineCriteria.distinct();
    }

    private static Condition<PipelineCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getNameSearch()) &&
                condition.apply(criteria.getModule()) &&
                condition.apply(criteria.getIsDefault()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PipelineCriteria> copyFiltersAre(PipelineCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getNameSearch(), copy.getNameSearch()) &&
                condition.apply(criteria.getModule(), copy.getModule()) &&
                condition.apply(criteria.getIsDefault(), copy.getIsDefault()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
