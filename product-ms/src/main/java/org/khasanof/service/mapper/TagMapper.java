package org.khasanof.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.khasanof.domain.Product;
import org.khasanof.domain.Tag;
import org.khasanof.service.dto.ProductDTO;
import org.khasanof.service.dto.TagDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tag} and its DTO {@link TagDTO}.
 */
@Mapper(componentModel = "spring")
public interface TagMapper extends EntityMapper<TagDTO, Tag> {
    @Mapping(target = "products", source = "products", qualifiedByName = "productIdSet")
    TagDTO toDto(Tag s);

    @Mapping(target = "products", ignore = true)
    @Mapping(target = "removeProduct", ignore = true)
    Tag toEntity(TagDTO tagDTO);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);

    @Named("productIdSet")
    default Set<ProductDTO> toDtoProductIdSet(Set<Product> product) {
        return product.stream().map(this::toDtoProductId).collect(Collectors.toSet());
    }
}
