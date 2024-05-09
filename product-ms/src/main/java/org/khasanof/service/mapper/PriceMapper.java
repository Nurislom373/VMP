package org.khasanof.service.mapper;

import org.khasanof.domain.Price;
import org.khasanof.service.dto.PriceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Price} and its DTO {@link PriceDTO}.
 */
@Mapper(componentModel = "spring")
public interface PriceMapper extends EntityMapper<PriceDTO, Price> {}
