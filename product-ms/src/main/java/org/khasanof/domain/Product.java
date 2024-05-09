package org.khasanof.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.khasanof.domain.enumeration.Level;
import org.khasanof.domain.enumeration.ProductStatus;
import org.khasanof.domain.enumeration.Visibility;

/**
 * A Product.
 */
@Entity
@Table(name = "product")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Product implements Serializable {

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

    @NotNull
    @Column(name = "has_options", nullable = false)
    private Boolean hasOptions;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProductStatus status;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @JsonIgnoreProperties(value = { "variants", "product" }, allowSetters = true)
    private Set<Option> options = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    @JsonIgnoreProperties(value = { "product" }, allowSetters = true)
    private Set<Image> images = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "rel_product__tag", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private Set<Tag> tags = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_product__gift",
        joinColumns = @JoinColumn(name = "product_id"),
        inverseJoinColumns = @JoinColumn(name = "gift_id")
    )
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private Set<Gift> gifts = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "products" }, allowSetters = true)
    private Category category;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Product id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Product name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public Product description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Visibility getVisibility() {
        return this.visibility;
    }

    public Product visibility(Visibility visibility) {
        this.setVisibility(visibility);
        return this;
    }

    public void setVisibility(Visibility visibility) {
        this.visibility = visibility;
    }

    public Level getLevel() {
        return this.level;
    }

    public Product level(Level level) {
        this.setLevel(level);
        return this;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public Long getStock() {
        return this.stock;
    }

    public Product stock(Long stock) {
        this.setStock(stock);
        return this;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public Boolean getUnlimitedStock() {
        return this.unlimitedStock;
    }

    public Product unlimitedStock(Boolean unlimitedStock) {
        this.setUnlimitedStock(unlimitedStock);
        return this;
    }

    public void setUnlimitedStock(Boolean unlimitedStock) {
        this.unlimitedStock = unlimitedStock;
    }

    public Boolean getHasOptions() {
        return this.hasOptions;
    }

    public Product hasOptions(Boolean hasOptions) {
        this.setHasOptions(hasOptions);
        return this;
    }

    public void setHasOptions(Boolean hasOptions) {
        this.hasOptions = hasOptions;
    }

    public ProductStatus getStatus() {
        return this.status;
    }

    public Product status(ProductStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ProductStatus status) {
        this.status = status;
    }

    public Set<Option> getOptions() {
        return this.options;
    }

    public void setOptions(Set<Option> options) {
        if (this.options != null) {
            this.options.forEach(i -> i.setProduct(null));
        }
        if (options != null) {
            options.forEach(i -> i.setProduct(this));
        }
        this.options = options;
    }

    public Product options(Set<Option> options) {
        this.setOptions(options);
        return this;
    }

    public Product addOption(Option option) {
        this.options.add(option);
        option.setProduct(this);
        return this;
    }

    public Product removeOption(Option option) {
        this.options.remove(option);
        option.setProduct(null);
        return this;
    }

    public Set<Image> getImages() {
        return this.images;
    }

    public void setImages(Set<Image> images) {
        if (this.images != null) {
            this.images.forEach(i -> i.setProduct(null));
        }
        if (images != null) {
            images.forEach(i -> i.setProduct(this));
        }
        this.images = images;
    }

    public Product images(Set<Image> images) {
        this.setImages(images);
        return this;
    }

    public Product addImage(Image image) {
        this.images.add(image);
        image.setProduct(this);
        return this;
    }

    public Product removeImage(Image image) {
        this.images.remove(image);
        image.setProduct(null);
        return this;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Product tags(Set<Tag> tags) {
        this.setTags(tags);
        return this;
    }

    public Product addTag(Tag tag) {
        this.tags.add(tag);
        return this;
    }

    public Product removeTag(Tag tag) {
        this.tags.remove(tag);
        return this;
    }

    public Set<Gift> getGifts() {
        return this.gifts;
    }

    public void setGifts(Set<Gift> gifts) {
        this.gifts = gifts;
    }

    public Product gifts(Set<Gift> gifts) {
        this.setGifts(gifts);
        return this;
    }

    public Product addGift(Gift gift) {
        this.gifts.add(gift);
        return this;
    }

    public Product removeGift(Gift gift) {
        this.gifts.remove(gift);
        return this;
    }

    public Category getCategory() {
        return this.category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Product category(Category category) {
        this.setCategory(category);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Product)) {
            return false;
        }
        return getId() != null && getId().equals(((Product) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Product{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", visibility='" + getVisibility() + "'" +
            ", level='" + getLevel() + "'" +
            ", stock=" + getStock() +
            ", unlimitedStock='" + getUnlimitedStock() + "'" +
            ", hasOptions='" + getHasOptions() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
