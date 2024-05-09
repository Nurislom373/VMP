package org.khasanof.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class TagCriteriaTest {

    @Test
    void newTagCriteriaHasAllFiltersNullTest() {
        var tagCriteria = new TagCriteria();
        assertThat(tagCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void tagCriteriaFluentMethodsCreatesFiltersTest() {
        var tagCriteria = new TagCriteria();

        setAllFilters(tagCriteria);

        assertThat(tagCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void tagCriteriaCopyCreatesNullFilterTest() {
        var tagCriteria = new TagCriteria();
        var copy = tagCriteria.copy();

        assertThat(tagCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(tagCriteria)
        );
    }

    @Test
    void tagCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var tagCriteria = new TagCriteria();
        setAllFilters(tagCriteria);

        var copy = tagCriteria.copy();

        assertThat(tagCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(tagCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var tagCriteria = new TagCriteria();

        assertThat(tagCriteria).hasToString("TagCriteria{}");
    }

    private static void setAllFilters(TagCriteria tagCriteria) {
        tagCriteria.id();
        tagCriteria.name();
        tagCriteria.status();
        tagCriteria.productId();
        tagCriteria.distinct();
    }

    private static Condition<TagCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<TagCriteria> copyFiltersAre(TagCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
