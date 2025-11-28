package com.mgs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class ReportRunCriteriaTest {

    @Test
    void newReportRunCriteriaHasAllFiltersNullTest() {
        var reportRunCriteria = new ReportRunCriteria();
        assertThat(reportRunCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void reportRunCriteriaFluentMethodsCreatesFiltersTest() {
        var reportRunCriteria = new ReportRunCriteria();

        setAllFilters(reportRunCriteria);

        assertThat(reportRunCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void reportRunCriteriaCopyCreatesNullFilterTest() {
        var reportRunCriteria = new ReportRunCriteria();
        var copy = reportRunCriteria.copy();

        assertThat(reportRunCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(reportRunCriteria)
        );
    }

    @Test
    void reportRunCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var reportRunCriteria = new ReportRunCriteria();
        setAllFilters(reportRunCriteria);

        var copy = reportRunCriteria.copy();

        assertThat(reportRunCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(reportRunCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var reportRunCriteria = new ReportRunCriteria();

        assertThat(reportRunCriteria).hasToString("ReportRunCriteria{}");
    }

    private static void setAllFilters(ReportRunCriteria reportRunCriteria) {
        reportRunCriteria.id();
        reportRunCriteria.name();
        reportRunCriteria.filterJson();
        reportRunCriteria.format();
        reportRunCriteria.filePath();
        reportRunCriteria.tenantId();
        reportRunCriteria.templateId();
        reportRunCriteria.generatedByUserId();
        reportRunCriteria.distinct();
    }

    private static Condition<ReportRunCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getFilterJson()) &&
                condition.apply(criteria.getFormat()) &&
                condition.apply(criteria.getFilePath()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getTemplateId()) &&
                condition.apply(criteria.getGeneratedByUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<ReportRunCriteria> copyFiltersAre(ReportRunCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getFilterJson(), copy.getFilterJson()) &&
                condition.apply(criteria.getFormat(), copy.getFormat()) &&
                condition.apply(criteria.getFilePath(), copy.getFilePath()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getTemplateId(), copy.getTemplateId()) &&
                condition.apply(criteria.getGeneratedByUserId(), copy.getGeneratedByUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
