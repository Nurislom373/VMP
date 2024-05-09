package org.khasanof.service.mapper;

import org.khasanof.domain.Image;
import org.khasanof.domain.Product;
import org.khasanof.service.dto.ImageDTO;
import org.khasanof.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Image} and its DTO {@link ImageDTO}.
 */
@Mapper(componentModel = "spring")
public interface ImageMapper extends EntityMapper<ImageDTO, Image> {
    @Mapping(target = "product", source = "product", qualifiedByName = "productId")
    ImageDTO toDto(Image s);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);
}
