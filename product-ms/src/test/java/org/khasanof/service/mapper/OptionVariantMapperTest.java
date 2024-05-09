package org.khasanof.service.mapper;

import static org.khasanof.domain.OptionVariantAsserts.*;
import static org.khasanof.domain.OptionVariantTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class OptionVariantMapperTest {

    private OptionVariantMapper optionVariantMapper;

    @BeforeEach
    void setUp() {
        optionVariantMapper = new OptionVariantMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getOptionVariantSample1();
        var actual = optionVariantMapper.toEntity(optionVariantMapper.toDto(expected));
        assertOptionVariantAllPropertiesEquals(expected, actual);
    }
}
