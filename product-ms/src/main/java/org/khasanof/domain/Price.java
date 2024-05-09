package org.khasanof.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

/**
 * A Price.
 */
@Entity
@Table(name = "price")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Price implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "price", precision = 21, scale = 2, nullable = false)
    private BigDecimal price;

    @NotNull
    @Column(name = "is_base", nullable = false)
    private Boolean isBase;

    @Column(name = "sku")
    private String sku;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "price")
    @JsonIgnoreProperties(value = { "price", "option" }, allowSetters = true)
    private Set<OptionVariant> variants = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Price id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public Price price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getIsBase() {
        return this.isBase;
    }

    public Price isBase(Boolean isBase) {
        this.setIsBase(isBase);
        return this;
    }

    public void setIsBase(Boolean isBase) {
        this.isBase = isBase;
    }

    public String getSku() {
        return this.sku;
    }

    public Price sku(String sku) {
        this.setSku(sku);
        return this;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public Set<OptionVariant> getVariants() {
        return this.variants;
    }

    public void setVariants(Set<OptionVariant> optionVariants) {
        if (this.variants != null) {
            this.variants.forEach(i -> i.setPrice(null));
        }
        if (optionVariants != null) {
            optionVariants.forEach(i -> i.setPrice(this));
        }
        this.variants = optionVariants;
    }

    public Price variants(Set<OptionVariant> optionVariants) {
        this.setVariants(optionVariants);
        return this;
    }

    public Price addVariants(OptionVariant optionVariant) {
        this.variants.add(optionVariant);
        optionVariant.setPrice(this);
        return this;
    }

    public Price removeVariants(OptionVariant optionVariant) {
        this.variants.remove(optionVariant);
        optionVariant.setPrice(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Price)) {
            return false;
        }
        return getId() != null && getId().equals(((Price) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Price{" +
            "id=" + getId() +
            ", price=" + getPrice() +
            ", isBase='" + getIsBase() + "'" +
            ", sku='" + getSku() + "'" +
            "}";
    }
}
