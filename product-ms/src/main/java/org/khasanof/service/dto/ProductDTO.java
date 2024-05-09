package org.khasanof.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.khasanof.domain.enumeration.Level;
import org.khasanof.domain.enumeration.ProductStatus;
import org.khasanof.domain.enumeration.Visibility;

/**
 * A DTO for the {@link org.khasanof.domain.Product} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @Lob
    private String description;

    private Visibility visibility;

    private Level level;

    @NotNull
    private Long stock;

    @NotNull
    private Boolean unlimitedStock;

    @NotNull
    private Boolean hasOptions;

    private ProductStatus status;

    private Set<TagDTO> tags = new HashSet<>();

    private Set<GiftDTO> gifts = new HashSet<>();

    private CategoryDTO category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Visibility getVisibility() {
        return visibility;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public Boolean getUnlimitedStock() {
        return unlimitedStock;
    }

    public void setUnlimitedStock(Boolean unlimitedStock) {
        this.unlimitedStock = unlimitedStock;
    }

    public Boolean getHasOptions() {
        return hasOptions;
    }

    public void setHasOptions(Boolean hasOptions) {
        this.hasOptions = hasOptions;
    }

    public ProductStatus getStatus() {
        return status;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public Set<TagDTO> getTags() {
        return tags;
    }

    public void setTags(Set<TagDTO> tags) {
        this.tags = tags;
    }

    public Set<GiftDTO> getGifts() {
        return gifts;
    }

    public void setGifts(Set<GiftDTO> gifts) {
        this.gifts = gifts;
    }

    public CategoryDTO getCategory() {
        return category;
    }

    public void setCategory(CategoryDTO category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProductDTO)) {
            return false;
        }

        ProductDTO productDTO = (ProductDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, productDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", visibility='" + getVisibility() + "'" +
            ", level='" + getLevel() + "'" +
            ", stock=" + getStock() +
            ", unlimitedStock='" + getUnlimitedStock() + "'" +
            ", hasOptions='" + getHasOptions() + "'" +
            ", status='" + getStatus() + "'" +
            ", tags=" + getTags() +
            ", gifts=" + getGifts() +
            ", category=" + getCategory() +
            "}";
    }
}
