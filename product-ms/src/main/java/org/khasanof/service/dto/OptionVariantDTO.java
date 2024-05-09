package org.khasanof.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import org.khasanof.domain.enumeration.OptionVariantStatus;

/**
 * A DTO for the {@link org.khasanof.domain.OptionVariant} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OptionVariantDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private OptionVariantStatus status;

    private PriceDTO price;

    private OptionDTO option;

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

    public OptionVariantStatus getStatus() {
        return status;
    }

    public void setStatus(OptionVariantStatus status) {
        this.status = status;
    }

    public PriceDTO getPrice() {
        return price;
    }

    public void setPrice(PriceDTO price) {
        this.price = price;
    }

    public OptionDTO getOption() {
        return option;
    }

    public void setOption(OptionDTO option) {
        this.option = option;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OptionVariantDTO)) {
            return false;
        }

        OptionVariantDTO optionVariantDTO = (OptionVariantDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, optionVariantDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OptionVariantDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", status='" + getStatus() + "'" +
            ", price=" + getPrice() +
            ", option=" + getOption() +
            "}";
    }
}
