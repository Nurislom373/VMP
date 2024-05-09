package org.khasanof.service.mapper;

import org.khasanof.domain.Option;
import org.khasanof.domain.OptionVariant;
import org.khasanof.domain.Price;
import org.khasanof.service.dto.OptionDTO;
import org.khasanof.service.dto.OptionVariantDTO;
import org.khasanof.service.dto.PriceDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link OptionVariant} and its DTO {@link OptionVariantDTO}.
 */
@Mapper(componentModel = "spring")
public interface OptionVariantMapper extends EntityMapper<OptionVariantDTO, OptionVariant> {
    @Mapping(target = "price", source = "price", qualifiedByName = "priceId")
    @Mapping(target = "option", source = "option", qualifiedByName = "optionId")
    OptionVariantDTO toDto(OptionVariant s);

    @Named("priceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PriceDTO toDtoPriceId(Price price);

    @Named("optionId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OptionDTO toDtoOptionId(Option option);
}
