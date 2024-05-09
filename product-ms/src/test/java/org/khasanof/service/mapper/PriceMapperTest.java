package org.khasanof.service.mapper;

import static org.khasanof.domain.PriceAsserts.*;
import static org.khasanof.domain.PriceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PriceMapperTest {

    private PriceMapper priceMapper;

    @BeforeEach
    void setUp() {
        priceMapper = new PriceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getPriceSample1();
        var actual = priceMapper.toEntity(priceMapper.toDto(expected));
        assertPriceAllPropertiesEquals(expected, actual);
    }
}
