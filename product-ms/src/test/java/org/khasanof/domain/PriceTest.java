package org.khasanof.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.khasanof.domain.OptionVariantTestSamples.*;
import static org.khasanof.domain.PriceTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.khasanof.web.rest.TestUtil;

class PriceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Price.class);
        Price price1 = getPriceSample1();
        Price price2 = new Price();
        assertThat(price1).isNotEqualTo(price2);

        price2.setId(price1.getId());
        assertThat(price1).isEqualTo(price2);

        price2 = getPriceSample2();
        assertThat(price1).isNotEqualTo(price2);
    }

    @Test
    void variantsTest() throws Exception {
        Price price = getPriceRandomSampleGenerator();
        OptionVariant optionVariantBack = getOptionVariantRandomSampleGenerator();

        price.addVariants(optionVariantBack);
        assertThat(price.getVariants()).containsOnly(optionVariantBack);
        assertThat(optionVariantBack.getPrice()).isEqualTo(price);

        price.removeVariants(optionVariantBack);
        assertThat(price.getVariants()).doesNotContain(optionVariantBack);
        assertThat(optionVariantBack.getPrice()).isNull();

        price.variants(new HashSet<>(Set.of(optionVariantBack)));
        assertThat(price.getVariants()).containsOnly(optionVariantBack);
        assertThat(optionVariantBack.getPrice()).isEqualTo(price);

        price.setVariants(new HashSet<>());
        assertThat(price.getVariants()).doesNotContain(optionVariantBack);
        assertThat(optionVariantBack.getPrice()).isNull();
    }
}
