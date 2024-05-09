package org.khasanof.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.khasanof.web.rest.TestUtil;

class GiftDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(GiftDTO.class);
        GiftDTO giftDTO1 = new GiftDTO();
        giftDTO1.setId(1L);
        GiftDTO giftDTO2 = new GiftDTO();
        assertThat(giftDTO1).isNotEqualTo(giftDTO2);
        giftDTO2.setId(giftDTO1.getId());
        assertThat(giftDTO1).isEqualTo(giftDTO2);
        giftDTO2.setId(2L);
        assertThat(giftDTO1).isNotEqualTo(giftDTO2);
        giftDTO1.setId(null);
        assertThat(giftDTO1).isNotEqualTo(giftDTO2);
    }
}
