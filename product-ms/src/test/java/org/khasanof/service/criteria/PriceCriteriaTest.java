package org.khasanof.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class PriceCriteriaTest {

    @Test
    void newPriceCriteriaHasAllFiltersNullTest() {
        var priceCriteria = new PriceCriteria();
        assertThat(priceCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void priceCriteriaFluentMethodsCreatesFiltersTest() {
        var priceCriteria = new PriceCriteria();

        setAllFilters(priceCriteria);

        assertThat(priceCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void priceCriteriaCopyCreatesNullFilterTest() {
        var priceCriteria = new PriceCriteria();
        var copy = priceCriteria.copy();

        assertThat(priceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(priceCriteria)
        );
    }

    @Test
    void priceCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var priceCriteria = new PriceCriteria();
        setAllFilters(priceCriteria);

        var copy = priceCriteria.copy();

        assertThat(priceCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(priceCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var priceCriteria = new PriceCriteria();

        assertThat(priceCriteria).hasToString("PriceCriteria{}");
    }

    private static void setAllFilters(PriceCriteria priceCriteria) {
        priceCriteria.id();
        priceCriteria.price();
        priceCriteria.isBase();
        priceCriteria.sku();
        priceCriteria.variantsId();
        priceCriteria.distinct();
    }

    private static Condition<PriceCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPrice()) &&
                condition.apply(criteria.getIsBase()) &&
                condition.apply(criteria.getSku()) &&
                condition.apply(criteria.getVariantsId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<PriceCriteria> copyFiltersAre(PriceCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPrice(), copy.getPrice()) &&
                condition.apply(criteria.getIsBase(), copy.getIsBase()) &&
                condition.apply(criteria.getSku(), copy.getSku()) &&
                condition.apply(criteria.getVariantsId(), copy.getVariantsId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
