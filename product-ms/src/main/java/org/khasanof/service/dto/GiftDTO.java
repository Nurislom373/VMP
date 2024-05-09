package org.khasanof.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.khasanof.domain.enumeration.GiftStatus;
import org.khasanof.domain.enumeration.Level;
import org.khasanof.domain.enumeration.Visibility;

/**
 * A DTO for the {@link org.khasanof.domain.Gift} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GiftDTO implements Serializable {

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

    private GiftStatus status;

    private Set<ProductDTO> products = new HashSet<>();

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

    public GiftStatus getStatus() {
        return status;
    }

    public void setStatus(GiftStatus status) {
        this.status = status;
    }

    public Set<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(Set<ProductDTO> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GiftDTO)) {
            return false;
        }

        GiftDTO giftDTO = (GiftDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, giftDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GiftDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", visibility='" + getVisibility() + "'" +
            ", level='" + getLevel() + "'" +
            ", stock=" + getStock() +
            ", unlimitedStock='" + getUnlimitedStock() + "'" +
            ", status='" + getStatus() + "'" +
            ", products=" + getProducts() +
            "}";
    }
}
