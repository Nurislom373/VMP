package org.khasanof.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class OptionCriteriaTest {

    @Test
    void newOptionCriteriaHasAllFiltersNullTest() {
        var optionCriteria = new OptionCriteria();
        assertThat(optionCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void optionCriteriaFluentMethodsCreatesFiltersTest() {
        var optionCriteria = new OptionCriteria();

        setAllFilters(optionCriteria);

        assertThat(optionCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void optionCriteriaCopyCreatesNullFilterTest() {
        var optionCriteria = new OptionCriteria();
        var copy = optionCriteria.copy();

        assertThat(optionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(optionCriteria)
        );
    }

    @Test
    void optionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var optionCriteria = new OptionCriteria();
        setAllFilters(optionCriteria);

        var copy = optionCriteria.copy();

        assertThat(optionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(optionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var optionCriteria = new OptionCriteria();

        assertThat(optionCriteria).hasToString("OptionCriteria{}");
    }

    private static void setAllFilters(OptionCriteria optionCriteria) {
        optionCriteria.id();
        optionCriteria.name();
        optionCriteria.status();
        optionCriteria.variantsId();
        optionCriteria.productId();
        optionCriteria.distinct();
    }

    private static Condition<OptionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getVariantsId()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<OptionCriteria> copyFiltersAre(OptionCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getVariantsId(), copy.getVariantsId()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
