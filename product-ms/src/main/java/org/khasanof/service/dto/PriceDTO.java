package org.khasanof.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A DTO for the {@link org.khasanof.domain.Price} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PriceDTO implements Serializable {

    private Long id;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Boolean isBase;

    private String sku;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getIsBase() {
        return isBase;
    }

    public void setIsBase(Boolean isBase) {
        this.isBase = isBase;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PriceDTO)) {
            return false;
        }

        PriceDTO priceDTO = (PriceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, priceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PriceDTO{" +
            "id=" + getId() +
            ", price=" + getPrice() +
            ", isBase='" + getIsBase() + "'" +
            ", sku='" + getSku() + "'" +
            "}";
    }
}
