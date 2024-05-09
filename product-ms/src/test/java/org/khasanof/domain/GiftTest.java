package org.khasanof.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.khasanof.domain.GiftTestSamples.*;
import static org.khasanof.domain.ProductTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.khasanof.web.rest.TestUtil;

class GiftTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Gift.class);
        Gift gift1 = getGiftSample1();
        Gift gift2 = new Gift();
        assertThat(gift1).isNotEqualTo(gift2);

        gift2.setId(gift1.getId());
        assertThat(gift1).isEqualTo(gift2);

        gift2 = getGiftSample2();
        assertThat(gift1).isNotEqualTo(gift2);
    }

    @Test
    void productTest() throws Exception {
        Gift gift = getGiftRandomSampleGenerator();
        Product productBack = getProductRandomSampleGenerator();

        gift.addProduct(productBack);
        assertThat(gift.getProducts()).containsOnly(productBack);
        assertThat(productBack.getGifts()).containsOnly(gift);

        gift.removeProduct(productBack);
        assertThat(gift.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getGifts()).doesNotContain(gift);

        gift.products(new HashSet<>(Set.of(productBack)));
        assertThat(gift.getProducts()).containsOnly(productBack);
        assertThat(productBack.getGifts()).containsOnly(gift);

        gift.setProducts(new HashSet<>());
        assertThat(gift.getProducts()).doesNotContain(productBack);
        assertThat(productBack.getGifts()).doesNotContain(gift);
    }
}
