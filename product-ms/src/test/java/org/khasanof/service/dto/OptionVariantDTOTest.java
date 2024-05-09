package org.khasanof.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.khasanof.web.rest.TestUtil;

class OptionVariantDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OptionVariantDTO.class);
        OptionVariantDTO optionVariantDTO1 = new OptionVariantDTO();
        optionVariantDTO1.setId(1L);
        OptionVariantDTO optionVariantDTO2 = new OptionVariantDTO();
        assertThat(optionVariantDTO1).isNotEqualTo(optionVariantDTO2);
        optionVariantDTO2.setId(optionVariantDTO1.getId());
        assertThat(optionVariantDTO1).isEqualTo(optionVariantDTO2);
        optionVariantDTO2.setId(2L);
        assertThat(optionVariantDTO1).isNotEqualTo(optionVariantDTO2);
        optionVariantDTO1.setId(null);
        assertThat(optionVariantDTO1).isNotEqualTo(optionVariantDTO2);
    }
}
