package org.khasanof.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.khasanof.domain.CategoryTestSamples.*;
import static org.khasanof.domain.GiftTestSamples.*;
import static org.khasanof.domain.ImageTestSamples.*;
import static org.khasanof.domain.OptionTestSamples.*;
import static org.khasanof.domain.ProductTestSamples.*;
import static org.khasanof.domain.TagTestSamples.*;

import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;
import org.khasanof.web.rest.TestUtil;

class ProductTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Product.class);
        Product product1 = getProductSample1();
        Product product2 = new Product();
        assertThat(product1).isNotEqualTo(product2);

        product2.setId(product1.getId());
        assertThat(product1).isEqualTo(product2);

        product2 = getProductSample2();
        assertThat(product1).isNotEqualTo(product2);
    }

    @Test
    void optionTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        Option optionBack = getOptionRandomSampleGenerator();

        product.addOption(optionBack);
        assertThat(product.getOptions()).containsOnly(optionBack);
        assertThat(optionBack.getProduct()).isEqualTo(product);

        product.removeOption(optionBack);
        assertThat(product.getOptions()).doesNotContain(optionBack);
        assertThat(optionBack.getProduct()).isNull();

        product.options(new HashSet<>(Set.of(optionBack)));
        assertThat(product.getOptions()).containsOnly(optionBack);
        assertThat(optionBack.getProduct()).isEqualTo(product);

        product.setOptions(new HashSet<>());
        assertThat(product.getOptions()).doesNotContain(optionBack);
        assertThat(optionBack.getProduct()).isNull();
    }

    @Test
    void imageTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        Image imageBack = getImageRandomSampleGenerator();

        product.addImage(imageBack);
        assertThat(product.getImages()).containsOnly(imageBack);
        assertThat(imageBack.getProduct()).isEqualTo(product);

        product.removeImage(imageBack);
        assertThat(product.getImages()).doesNotContain(imageBack);
        assertThat(imageBack.getProduct()).isNull();

        product.images(new HashSet<>(Set.of(imageBack)));
        assertThat(product.getImages()).containsOnly(imageBack);
        assertThat(imageBack.getProduct()).isEqualTo(product);

        product.setImages(new HashSet<>());
        assertThat(product.getImages()).doesNotContain(imageBack);
        assertThat(imageBack.getProduct()).isNull();
    }

    @Test
    void tagTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        Tag tagBack = getTagRandomSampleGenerator();

        product.addTag(tagBack);
        assertThat(product.getTags()).containsOnly(tagBack);

        product.removeTag(tagBack);
        assertThat(product.getTags()).doesNotContain(tagBack);

        product.tags(new HashSet<>(Set.of(tagBack)));
        assertThat(product.getTags()).containsOnly(tagBack);

        product.setTags(new HashSet<>());
        assertThat(product.getTags()).doesNotContain(tagBack);
    }

    @Test
    void giftTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        Gift giftBack = getGiftRandomSampleGenerator();

        product.addGift(giftBack);
        assertThat(product.getGifts()).containsOnly(giftBack);

        product.removeGift(giftBack);
        assertThat(product.getGifts()).doesNotContain(giftBack);

        product.gifts(new HashSet<>(Set.of(giftBack)));
        assertThat(product.getGifts()).containsOnly(giftBack);

        product.setGifts(new HashSet<>());
        assertThat(product.getGifts()).doesNotContain(giftBack);
    }

    @Test
    void categoryTest() throws Exception {
        Product product = getProductRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        product.setCategory(categoryBack);
        assertThat(product.getCategory()).isEqualTo(categoryBack);

        product.category(null);
        assertThat(product.getCategory()).isNull();
    }
}
