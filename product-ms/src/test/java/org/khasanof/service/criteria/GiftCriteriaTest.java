package org.khasanof.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class GiftCriteriaTest {

    @Test
    void newGiftCriteriaHasAllFiltersNullTest() {
        var giftCriteria = new GiftCriteria();
        assertThat(giftCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void giftCriteriaFluentMethodsCreatesFiltersTest() {
        var giftCriteria = new GiftCriteria();

        setAllFilters(giftCriteria);

        assertThat(giftCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void giftCriteriaCopyCreatesNullFilterTest() {
        var giftCriteria = new GiftCriteria();
        var copy = giftCriteria.copy();

        assertThat(giftCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(giftCriteria)
        );
    }

    @Test
    void giftCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var giftCriteria = new GiftCriteria();
        setAllFilters(giftCriteria);

        var copy = giftCriteria.copy();

        assertThat(giftCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(giftCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var giftCriteria = new GiftCriteria();

        assertThat(giftCriteria).hasToString("GiftCriteria{}");
    }

    private static void setAllFilters(GiftCriteria giftCriteria) {
        giftCriteria.id();
        giftCriteria.name();
        giftCriteria.visibility();
        giftCriteria.level();
        giftCriteria.stock();
        giftCriteria.unlimitedStock();
        giftCriteria.status();
        giftCriteria.productId();
        giftCriteria.distinct();
    }

    private static Condition<GiftCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getVisibility()) &&
                condition.apply(criteria.getLevel()) &&
                condition.apply(criteria.getStock()) &&
                condition.apply(criteria.getUnlimitedStock()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<GiftCriteria> copyFiltersAre(GiftCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getVisibility(), copy.getVisibility()) &&
                condition.apply(criteria.getLevel(), copy.getLevel()) &&
                condition.apply(criteria.getStock(), copy.getStock()) &&
                condition.apply(criteria.getUnlimitedStock(), copy.getUnlimitedStock()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
