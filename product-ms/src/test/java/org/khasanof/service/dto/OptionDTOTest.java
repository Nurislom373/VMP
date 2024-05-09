package org.khasanof.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.khasanof.web.rest.TestUtil;

class OptionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(OptionDTO.class);
        OptionDTO optionDTO1 = new OptionDTO();
        optionDTO1.setId(1L);
        OptionDTO optionDTO2 = new OptionDTO();
        assertThat(optionDTO1).isNotEqualTo(optionDTO2);
        optionDTO2.setId(optionDTO1.getId());
        assertThat(optionDTO1).isEqualTo(optionDTO2);
        optionDTO2.setId(2L);
        assertThat(optionDTO1).isNotEqualTo(optionDTO2);
        optionDTO1.setId(null);
        assertThat(optionDTO1).isNotEqualTo(optionDTO2);
    }
}
