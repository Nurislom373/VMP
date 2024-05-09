package org.khasanof.service.mapper;

import static org.khasanof.domain.ImageAsserts.*;
import static org.khasanof.domain.ImageTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ImageMapperTest {

    private ImageMapper imageMapper;

    @BeforeEach
    void setUp() {
        imageMapper = new ImageMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getImageSample1();
        var actual = imageMapper.toEntity(imageMapper.toDto(expected));
        assertImageAllPropertiesEquals(expected, actual);
    }
}
