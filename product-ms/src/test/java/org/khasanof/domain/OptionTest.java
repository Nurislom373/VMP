package org.khasanof.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.khasanof.domain.OptionTestSamples.*;
import static org.khasanof.domain.OptionVariantTestSamples.*;
import static org.khasanof.domain.ProductTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.khasanof.web.rest.TestUtil;

class OptionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Option.class);
        Option option1 = getOptionSample1();
        Option option2 = new Option();
        assertThat(option1).isNotEqualTo(option2);

        option2.setId(option1.getId());
        assertThat(option1).isEqualTo(option2);

        option2 = getOptionSample2();
        assertThat(option1).isNotEqualTo(option2);
    }

    @Test
    void variantsTest() throws Exception {
        Option option = getOptionRandomSampleGenerator();
        OptionVariant optionVariantBack = getOptionVariantRandomSampleGenerator();

        option.addVariants(optionVariantBack);
        assertThat(option.getVariants()).containsOnly(optionVariantBack);
        assertThat(optionVariantBack.getOption()).isEqualTo(option);

        option.removeVariants(optionVariantBack);
        assertThat(option.getVariants()).doesNotContain(optionVariantBack);
        assertThat(optionVariantBack.getOption()).isNull();

        option.variants(new HashSet<>(Set.of(optionVariantBack)));
        assertThat(option.getVariants()).containsOnly(optionVariantBack);
        assertThat(optionVariantBack.getOption()).isEqualTo(option);

        option.setVariants(new HashSet<>());
        assertThat(option.getVariants()).doesNotContain(optionVariantBack);
        assertThat(optionVariantBack.getOption()).isNull();
    }

    @Test
    void productTest() throws Exception {
        Option option = getOptionRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        option.setProduct(productBack);
        assertThat(option.getProduct()).isEqualTo(productBack);

        option.product(null);
        assertThat(option.getProduct()).isNull();
    }
}
