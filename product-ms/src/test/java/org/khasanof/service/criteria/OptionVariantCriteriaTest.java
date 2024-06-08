package org.khasanof.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OptionVariantCriteriaTest {

    @Test
    void newOptionVariantCriteriaHasAllFiltersNullTest() {
        var optionVariantCriteria = new OptionVariantCriteria();
        assertThat(optionVariantCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void optionVariantCriteriaFluentMethodsCreatesFiltersTest() {
        var optionVariantCriteria = new OptionVariantCriteria();

        setAllFilters(optionVariantCriteria);

        assertThat(optionVariantCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void optionVariantCriteriaCopyCreatesNullFilterTest() {
        var optionVariantCriteria = new OptionVariantCriteria();
        var copy = optionVariantCriteria.copy();

        assertThat(optionVariantCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(optionVariantCriteria)
        );
    }

    @Test
    void optionVariantCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var optionVariantCriteria = new OptionVariantCriteria();
        setAllFilters(optionVariantCriteria);

        var copy = optionVariantCriteria.copy();

        assertThat(optionVariantCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(optionVariantCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var optionVariantCriteria = new OptionVariantCriteria();

        assertThat(optionVariantCriteria).hasToString("OptionVariantCriteria{}");
    }

    private static void setAllFilters(OptionVariantCriteria optionVariantCriteria) {
        optionVariantCriteria.id();
        optionVariantCriteria.name();
        optionVariantCriteria.stock();
        optionVariantCriteria.status();
        optionVariantCriteria.priceId();
        optionVariantCriteria.optionId();
        optionVariantCriteria.distinct();
    }

    private static Condition<OptionVariantCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getStock()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getPriceId()) &&
                condition.apply(criteria.getOptionId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OptionVariantCriteria> copyFiltersAre(
        OptionVariantCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getStock(), copy.getStock()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getPriceId(), copy.getPriceId()) &&
                condition.apply(criteria.getOptionId(), copy.getOptionId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
