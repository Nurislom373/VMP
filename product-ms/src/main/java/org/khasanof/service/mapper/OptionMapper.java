package org.khasanof.service.mapper;

import org.khasanof.domain.Option;
import org.khasanof.domain.Product;
import org.khasanof.service.dto.OptionDTO;
import org.khasanof.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Option} and its DTO {@link OptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface OptionMapper extends EntityMapper<OptionDTO, Option> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    OptionDTO toDto(Option s);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);
}
