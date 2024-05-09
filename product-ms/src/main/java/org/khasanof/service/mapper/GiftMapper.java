package org.khasanof.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.khasanof.domain.Gift;
import org.khasanof.domain.Product;
import org.khasanof.service.dto.GiftDTO;
import org.khasanof.service.dto.ProductDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Gift} and its DTO {@link GiftDTO}.
 */
@Mapper(componentModel = "spring")
public interface GiftMapper extends EntityMapper<GiftDTO, Gift> {
    @Mapping(target = "products", source = "products", qualifiedByName = "productIdSet")
    GiftDTO toDto(Gift s);

    @Mapping(target = "products", ignore = true)
    @Mapping(target = "removeProduct", ignore = true)
    Gift toEntity(GiftDTO giftDTO);

    @Named("productId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ProductDTO toDtoProductId(Product product);

    @Named("productIdSet")
    default Set<ProductDTO> toDtoProductIdSet(Set<Product> product) {
        return product.stream().map(this::toDtoProductId).collect(Collectors.toSet());
    }
}
