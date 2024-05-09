package org.khasanof.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.khasanof.domain.enumeration.Level;
import org.khasanof.domain.enumeration.ProductStatus;
import org.khasanof.domain.enumeration.Visibility;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.khasanof.domain.Product} entity. This class is used
 * in {@link org.khasanof.web.rest.ProductResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /products?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProductCriteria implements Serializable, Criteria {

    /**
     * Class for filtering Visibility
     */
    public static class VisibilityFilter extends Filter<Visibility> {

        public VisibilityFilter() {}

        public VisibilityFilter(VisibilityFilter filter) {
            super(filter);
        }

        @Override
        public VisibilityFilter copy() {
            return new VisibilityFilter(this);
        }
    }

    /**
     * Class for filtering Level
     */
    public static class LevelFilter extends Filter<Level> {

        public LevelFilter() {}

        public LevelFilter(LevelFilter filter) {
            super(filter);
        }

        @Override
        public LevelFilter copy() {
            return new LevelFilter(this);
        }
    }

    /**
     * Class for filtering ProductStatus
     */
    public static class ProductStatusFilter extends Filter<ProductStatus> {

        public ProductStatusFilter() {}

        public ProductStatusFilter(ProductStatusFilter filter) {
            super(filter);
        }

        @Override
        public ProductStatusFilter copy() {
            return new ProductStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private VisibilityFilter visibility;

    private LevelFilter level;

    private LongFilter stock;

    private BooleanFilter unlimitedStock;

    private BooleanFilter hasOptions;

    private ProductStatusFilter status;

    private LongFilter optionId;

    private LongFilter imageId;

    private LongFilter tagId;

    private LongFilter giftId;

    private LongFilter categoryId;

    private Boolean distinct;

    public ProductCriteria() {}

    public ProductCriteria(ProductCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.visibility = other.optionalVisibility().map(VisibilityFilter::copy).orElse(null);
        this.level = other.optionalLevel().map(LevelFilter::copy).orElse(null);
        this.stock = other.optionalStock().map(LongFilter::copy).orElse(null);
        this.unlimitedStock = other.optionalUnlimitedStock().map(BooleanFilter::copy).orElse(null);
        this.hasOptions = other.optionalHasOptions().map(BooleanFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(ProductStatusFilter::copy).orElse(null);
        this.optionId = other.optionalOptionId().map(LongFilter::copy).orElse(null);
        this.imageId = other.optionalImageId().map(LongFilter::copy).orElse(null);
        this.tagId = other.optionalTagId().map(LongFilter::copy).orElse(null);
        this.giftId = other.optionalGiftId().map(LongFilter::copy).orElse(null);
        this.categoryId = other.optionalCategoryId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ProductCriteria copy() {
        return new ProductCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public VisibilityFilter getVisibility() {
        return visibility;
    }

    public Optional<VisibilityFilter> optionalVisibility() {
        return Optional.ofNullable(visibility);
    }

    public VisibilityFilter visibility() {
        if (visibility == null) {
            setVisibility(new VisibilityFilter());
        }
        return visibility;
    }

    public void setVisibility(VisibilityFilter visibility) {
        this.visibility = visibility;
    }

    public LevelFilter getLevel() {
        return level;
    }

    public Optional<LevelFilter> optionalLevel() {
        return Optional.ofNullable(level);
    }

    public LevelFilter level() {
        if (level == null) {
            setLevel(new LevelFilter());
        }
        return level;
    }

    public void setLevel(LevelFilter level) {
        this.level = level;
    }

    public LongFilter getStock() {
        return stock;
    }

    public Optional<LongFilter> optionalStock() {
        return Optional.ofNullable(stock);
    }

    public LongFilter stock() {
        if (stock == null) {
            setStock(new LongFilter());
        }
        return stock;
    }

    public void setStock(LongFilter stock) {
        this.stock = stock;
    }

    public BooleanFilter getUnlimitedStock() {
        return unlimitedStock;
    }

    public Optional<BooleanFilter> optionalUnlimitedStock() {
        return Optional.ofNullable(unlimitedStock);
    }

    public BooleanFilter unlimitedStock() {
        if (unlimitedStock == null) {
            setUnlimitedStock(new BooleanFilter());
        }
        return unlimitedStock;
    }

    public void setUnlimitedStock(BooleanFilter unlimitedStock) {
        this.unlimitedStock = unlimitedStock;
    }

    public BooleanFilter getHasOptions() {
        return hasOptions;
    }

    public Optional<BooleanFilter> optionalHasOptions() {
        return Optional.ofNullable(hasOptions);
    }

    public BooleanFilter hasOptions() {
        if (hasOptions == null) {
            setHasOptions(new BooleanFilter());
        }
        return hasOptions;
    }

    public void setHasOptions(BooleanFilter hasOptions) {
        this.hasOptions = hasOptions;
    }

    public ProductStatusFilter getStatus() {
        return status;
    }

    public Optional<ProductStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public ProductStatusFilter status() {
        if (status == null) {
            setStatus(new ProductStatusFilter());
        }
        return status;
    }

    public void setStatus(ProductStatusFilter status) {
        this.status = status;
    }

    public LongFilter getOptionId() {
        return optionId;
    }

    public Optional<LongFilter> optionalOptionId() {
        return Optional.ofNullable(optionId);
    }

    public LongFilter optionId() {
        if (optionId == null) {
            setOptionId(new LongFilter());
        }
        return optionId;
    }

    public void setOptionId(LongFilter optionId) {
        this.optionId = optionId;
    }

    public LongFilter getImageId() {
        return imageId;
    }

    public Optional<LongFilter> optionalImageId() {
        return Optional.ofNullable(imageId);
    }

    public LongFilter imageId() {
        if (imageId == null) {
            setImageId(new LongFilter());
        }
        return imageId;
    }

    public void setImageId(LongFilter imageId) {
        this.imageId = imageId;
    }

    public LongFilter getTagId() {
        return tagId;
    }

    public Optional<LongFilter> optionalTagId() {
        return Optional.ofNullable(tagId);
    }

    public LongFilter tagId() {
        if (tagId == null) {
            setTagId(new LongFilter());
        }
        return tagId;
    }

    public void setTagId(LongFilter tagId) {
        this.tagId = tagId;
    }

    public LongFilter getGiftId() {
        return giftId;
    }

    public Optional<LongFilter> optionalGiftId() {
        return Optional.ofNullable(giftId);
    }

    public LongFilter giftId() {
        if (giftId == null) {
            setGiftId(new LongFilter());
        }
        return giftId;
    }

    public void setGiftId(LongFilter giftId) {
        this.giftId = giftId;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public Optional<LongFilter> optionalCategoryId() {
        return Optional.ofNullable(categoryId);
    }

    public LongFilter categoryId() {
        if (categoryId == null) {
            setCategoryId(new LongFilter());
        }
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final ProductCriteria that = (ProductCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(visibility, that.visibility) &&
            Objects.equals(level, that.level) &&
            Objects.equals(stock, that.stock) &&
            Objects.equals(unlimitedStock, that.unlimitedStock) &&
            Objects.equals(hasOptions, that.hasOptions) &&
            Objects.equals(status, that.status) &&
            Objects.equals(optionId, that.optionId) &&
            Objects.equals(imageId, that.imageId) &&
            Objects.equals(tagId, that.tagId) &&
            Objects.equals(giftId, that.giftId) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            visibility,
            level,
            stock,
            unlimitedStock,
            hasOptions,
            status,
            optionId,
            imageId,
            tagId,
            giftId,
            categoryId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProductCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalVisibility().map(f -> "visibility=" + f + ", ").orElse("") +
            optionalLevel().map(f -> "level=" + f + ", ").orElse("") +
            optionalStock().map(f -> "stock=" + f + ", ").orElse("") +
            optionalUnlimitedStock().map(f -> "unlimitedStock=" + f + ", ").orElse("") +
            optionalHasOptions().map(f -> "hasOptions=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalOptionId().map(f -> "optionId=" + f + ", ").orElse("") +
            optionalImageId().map(f -> "imageId=" + f + ", ").orElse("") +
            optionalTagId().map(f -> "tagId=" + f + ", ").orElse("") +
            optionalGiftId().map(f -> "giftId=" + f + ", ").orElse("") +
            optionalCategoryId().map(f -> "categoryId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
