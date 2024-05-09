package org.khasanof.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.khasanof.domain.enumeration.GiftStatus;
import org.khasanof.domain.enumeration.Level;
import org.khasanof.domain.enumeration.Visibility;

/**
 * A Gift.
 */
@Entity
@Table(name = "gift")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Gift implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Lob
    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "visibility")
    private Visibility visibility;

    @Enumerated(EnumType.STRING)
    @Column(name = "level")
    private Level level;

    @NotNull
    @Column(name = "stock", nullable = false)
    private Long stock;

    @NotNull
    @Column(name = "unlimited_stock", nullable = false)
    private Boolean unlimitedStock;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private GiftStatus status;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "gifts")
    @JsonIgnoreProperties(value = { "options", "images", "tags", "gifts", "category" }, allowSetters = true)
    private Set<Product> products = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Gift id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Gift name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Gift description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Visibility getVisibility() {
        return this.visibility;
    }

    public Gift visibility(Visibility visibility) {
        this.setVisibility(visibility);
        return this;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public Level getLevel() {
        return this.level;
    }

    public Gift level(Level level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Long getStock() {
        return this.stock;
    }

    public Gift stock(Long stock) {
        this.setStock(stock);
        return this;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public Boolean getUnlimitedStock() {
        return this.unlimitedStock;
    }

    public Gift unlimitedStock(Boolean unlimitedStock) {
        this.setUnlimitedStock(unlimitedStock);
        return this;
    }

    public void setUnlimitedStock(Boolean unlimitedStock) {
        this.unlimitedStock = unlimitedStock;
    }

    public GiftStatus getStatus() {
        return this.status;
    }

    public Gift status(GiftStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(GiftStatus status) {
        this.status = status;
    }

    public Set<Product> getProducts() {
        return this.products;
    }

    public void setProducts(Set<Product> products) {
        if (this.products != null) {
            this.products.forEach(i -> i.removeGift(this));
        }
        if (products != null) {
            products.forEach(i -> i.addGift(this));
        }
        this.products = products;
    }

    public Gift products(Set<Product> products) {
        this.setProducts(products);
        return this;
    }

    public Gift addProduct(Product product) {
        this.products.add(product);
        product.getGifts().add(this);
        return this;
    }

    public Gift removeProduct(Product product) {
        this.products.remove(product);
        product.getGifts().remove(this);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Gift)) {
            return false;
        }
        return getId() != null && getId().equals(((Gift) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Gift{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", visibility='" + getVisibility() + "'" +
            ", level='" + getLevel() + "'" +
            ", stock=" + getStock() +
            ", unlimitedStock='" + getUnlimitedStock() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
