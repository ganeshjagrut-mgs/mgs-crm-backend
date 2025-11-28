package com.mgs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class SubPipelineCriteriaTest {

    @Test
    void newSubPipelineCriteriaHasAllFiltersNullTest() {
        var subPipelineCriteria = new SubPipelineCriteria();
        assertThat(subPipelineCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void subPipelineCriteriaFluentMethodsCreatesFiltersTest() {
        var subPipelineCriteria = new SubPipelineCriteria();

        setAllFilters(subPipelineCriteria);

        assertThat(subPipelineCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void subPipelineCriteriaCopyCreatesNullFilterTest() {
        var subPipelineCriteria = new SubPipelineCriteria();
        var copy = subPipelineCriteria.copy();

        assertThat(subPipelineCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(subPipelineCriteria)
        );
    }

    @Test
    void subPipelineCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var subPipelineCriteria = new SubPipelineCriteria();
        setAllFilters(subPipelineCriteria);

        var copy = subPipelineCriteria.copy();

        assertThat(subPipelineCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(subPipelineCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var subPipelineCriteria = new SubPipelineCriteria();

        assertThat(subPipelineCriteria).hasToString("SubPipelineCriteria{}");
    }

    private static void setAllFilters(SubPipelineCriteria subPipelineCriteria) {
        subPipelineCriteria.id();
        subPipelineCriteria.name();
        subPipelineCriteria.nameSearch();
        subPipelineCriteria.sequenceOrder();
        subPipelineCriteria.probability();
        subPipelineCriteria.isClosingStage();
        subPipelineCriteria.tenantId();
        subPipelineCriteria.pipelineId();
        subPipelineCriteria.distinct();
    }

    private static Condition<SubPipelineCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getNameSearch()) &&
                condition.apply(criteria.getSequenceOrder()) &&
                condition.apply(criteria.getProbability()) &&
                condition.apply(criteria.getIsClosingStage()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getPipelineId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<SubPipelineCriteria> copyFiltersAre(SubPipelineCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getNameSearch(), copy.getNameSearch()) &&
                condition.apply(criteria.getSequenceOrder(), copy.getSequenceOrder()) &&
                condition.apply(criteria.getProbability(), copy.getProbability()) &&
                condition.apply(criteria.getIsClosingStage(), copy.getIsClosingStage()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getPipelineId(), copy.getPipelineId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
