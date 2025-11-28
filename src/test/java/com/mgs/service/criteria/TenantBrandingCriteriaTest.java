package com.mgs.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TenantBrandingCriteriaTest {

    @Test
    void newTenantBrandingCriteriaHasAllFiltersNullTest() {
        var tenantBrandingCriteria = new TenantBrandingCriteria();
        assertThat(tenantBrandingCriteria).is(criteriaFiltersAre(Objects::isNull));
    }

    @Test
    void tenantBrandingCriteriaFluentMethodsCreatesFiltersTest() {
        var tenantBrandingCriteria = new TenantBrandingCriteria();

        setAllFilters(tenantBrandingCriteria);

        assertThat(tenantBrandingCriteria).is(criteriaFiltersAre(Objects::nonNull));
    }

    @Test
    void tenantBrandingCriteriaCopyCreatesNullFilterTest() {
        var tenantBrandingCriteria = new TenantBrandingCriteria();
        var copy = tenantBrandingCriteria.copy();

        assertThat(tenantBrandingCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::isNull)),
            criteria -> assertThat(criteria).isEqualTo(tenantBrandingCriteria)
        );
    }

    @Test
    void tenantBrandingCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var tenantBrandingCriteria = new TenantBrandingCriteria();
        setAllFilters(tenantBrandingCriteria);

        var copy = tenantBrandingCriteria.copy();

        assertThat(tenantBrandingCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(Objects::nonNull)),
            criteria -> assertThat(criteria).isEqualTo(tenantBrandingCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var tenantBrandingCriteria = new TenantBrandingCriteria();

        assertThat(tenantBrandingCriteria).hasToString("TenantBrandingCriteria{}");
    }

    private static void setAllFilters(TenantBrandingCriteria tenantBrandingCriteria) {
        tenantBrandingCriteria.id();
        tenantBrandingCriteria.logoPath();
        tenantBrandingCriteria.logoDarkPath();
        tenantBrandingCriteria.faviconPath();
        tenantBrandingCriteria.primaryColor();
        tenantBrandingCriteria.secondaryColor();
        tenantBrandingCriteria.accentColor();
        tenantBrandingCriteria.pdfHeaderLogoPath();
        tenantBrandingCriteria.pdfFooterText();
        tenantBrandingCriteria.pdfPrimaryColor();
        tenantBrandingCriteria.tenantId();
        tenantBrandingCriteria.distinct();
    }

    private static Condition<TenantBrandingCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getLogoPath()) &&
                condition.apply(criteria.getLogoDarkPath()) &&
                condition.apply(criteria.getFaviconPath()) &&
                condition.apply(criteria.getPrimaryColor()) &&
                condition.apply(criteria.getSecondaryColor()) &&
                condition.apply(criteria.getAccentColor()) &&
                condition.apply(criteria.getPdfHeaderLogoPath()) &&
                condition.apply(criteria.getPdfFooterText()) &&
                condition.apply(criteria.getPdfPrimaryColor()) &&
                condition.apply(criteria.getTenantId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TenantBrandingCriteria> copyFiltersAre(
        TenantBrandingCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getLogoPath(), copy.getLogoPath()) &&
                condition.apply(criteria.getLogoDarkPath(), copy.getLogoDarkPath()) &&
                condition.apply(criteria.getFaviconPath(), copy.getFaviconPath()) &&
                condition.apply(criteria.getPrimaryColor(), copy.getPrimaryColor()) &&
                condition.apply(criteria.getSecondaryColor(), copy.getSecondaryColor()) &&
                condition.apply(criteria.getAccentColor(), copy.getAccentColor()) &&
                condition.apply(criteria.getPdfHeaderLogoPath(), copy.getPdfHeaderLogoPath()) &&
                condition.apply(criteria.getPdfFooterText(), copy.getPdfFooterText()) &&
                condition.apply(criteria.getPdfPrimaryColor(), copy.getPdfPrimaryColor()) &&
                condition.apply(criteria.getTenantId(), copy.getTenantId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
