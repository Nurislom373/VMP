package org.khasanof.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.khasanof.domain.OptionTestSamples.*;
import static org.khasanof.domain.OptionVariantTestSamples.*;
import static org.khasanof.domain.PriceTestSamples.*;

import org.junit.jupiter.api.Test;
import org.khasanof.web.rest.TestUtil;

class OptionVariantTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OptionVariant.class);
        OptionVariant optionVariant1 = getOptionVariantSample1();
        OptionVariant optionVariant2 = new OptionVariant();
        assertThat(optionVariant1).isNotEqualTo(optionVariant2);

        optionVariant2.setId(optionVariant1.getId());
        assertThat(optionVariant1).isEqualTo(optionVariant2);

        optionVariant2 = getOptionVariantSample2();
        assertThat(optionVariant1).isNotEqualTo(optionVariant2);
    }

    @Test
    void priceTest() throws Exception {
        OptionVariant optionVariant = getOptionVariantRandomSampleGenerator();
        Price priceBack = getPriceRandomSampleGenerator();

        optionVariant.setPrice(priceBack);
        assertThat(optionVariant.getPrice()).isEqualTo(priceBack);

        optionVariant.price(null);
        assertThat(optionVariant.getPrice()).isNull();
    }

    @Test
    void optionTest() throws Exception {
        OptionVariant optionVariant = getOptionVariantRandomSampleGenerator();
        Option optionBack = getOptionRandomSampleGenerator();

        optionVariant.setOption(optionBack);
        assertThat(optionVariant.getOption()).isEqualTo(optionBack);

        optionVariant.option(null);
        assertThat(optionVariant.getOption()).isNull();
    }
}
