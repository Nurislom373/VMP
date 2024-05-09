package org.khasanof.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.khasanof.domain.AssertUtils.bigDecimalCompareTo;

public class PriceAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPriceAllPropertiesEquals(Price expected, Price actual) {
        assertPriceAutoGeneratedPropertiesEquals(expected, actual);
        assertPriceAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPriceAllUpdatablePropertiesEquals(Price expected, Price actual) {
        assertPriceUpdatableFieldsEquals(expected, actual);
        assertPriceUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPriceAutoGeneratedPropertiesEquals(Price expected, Price actual) {
        assertThat(expected)
            .as("Verify Price auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPriceUpdatableFieldsEquals(Price expected, Price actual) {
        assertThat(expected)
            .as("Verify Price relevant properties")
            .satisfies(e -> assertThat(e.getPrice()).as("check price").usingComparator(bigDecimalCompareTo).isEqualTo(actual.getPrice()))
            .satisfies(e -> assertThat(e.getIsBase()).as("check isBase").isEqualTo(actual.getIsBase()))
            .satisfies(e -> assertThat(e.getSku()).as("check sku").isEqualTo(actual.getSku()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertPriceUpdatableRelationshipsEquals(Price expected, Price actual) {}
}