package org.khasanof.service.mapper;

import static org.khasanof.domain.GiftAsserts.*;
import static org.khasanof.domain.GiftTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GiftMapperTest {

    private GiftMapper giftMapper;

    @BeforeEach
    void setUp() {
        giftMapper = new GiftMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getGiftSample1();
        var actual = giftMapper.toEntity(giftMapper.toDto(expected));
        assertGiftAllPropertiesEquals(expected, actual);
    }
}
