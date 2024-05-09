package org.khasanof.service.mapper;

import java.util.Set;
import java.util.stream.Collectors;
import org.khasanof.domain.Category;
import org.khasanof.domain.Gift;
import org.khasanof.domain.Product;
import org.khasanof.domain.Tag;
import org.khasanof.service.dto.CategoryDTO;
import org.khasanof.service.dto.GiftDTO;
import org.khasanof.service.dto.ProductDTO;
import org.khasanof.service.dto.TagDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Product} and its DTO {@link ProductDTO}.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper extends EntityMapper<ProductDTO, Product> {
    @Mapping(target = "tags", source = "tags", qualifiedByName = "tagIdSet")
    @Mapping(target = "gifts", source = "gifts", qualifiedByName = "giftIdSet")
    @Mapping(target = "category", source = "category", qualifiedByName = "categoryId")
    ProductDTO toDto(Product s);

    @Mapping(target = "removeTag", ignore = true)
    @Mapping(target = "removeGift", ignore = true)
    Product toEntity(ProductDTO productDTO);

    @Named("tagId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TagDTO toDtoTagId(Tag tag);

    @Named("tagIdSet")
    default Set<TagDTO> toDtoTagIdSet(Set<Tag> tag) {
        return tag.stream().map(this::toDtoTagId).collect(Collectors.toSet());
    }

    @Named("giftId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    GiftDTO toDtoGiftId(Gift gift);

    @Named("giftIdSet")
    default Set<GiftDTO> toDtoGiftIdSet(Set<Gift> gift) {
        return gift.stream().map(this::toDtoGiftId).collect(Collectors.toSet());
    }

    @Named("categoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CategoryDTO toDtoCategoryId(Category category);
}
