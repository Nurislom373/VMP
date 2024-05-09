package org.khasanof.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.khasanof.domain.enumeration.GiftStatus;
import org.khasanof.domain.enumeration.Level;
import org.khasanof.domain.enumeration.Visibility;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.khasanof.domain.Gift} entity. This class is used
 * in {@link org.khasanof.web.rest.GiftResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /gifts?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class GiftCriteria implements Serializable, Criteria {

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
     * Class for filtering GiftStatus
     */
    public static class GiftStatusFilter extends Filter<GiftStatus> {

        public GiftStatusFilter() {}

        public GiftStatusFilter(GiftStatusFilter filter) {
            super(filter);
        }

        @Override
        public GiftStatusFilter copy() {
            return new GiftStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private VisibilityFilter visibility;

    private LevelFilter level;

    private LongFilter stock;

    private BooleanFilter unlimitedStock;

    private GiftStatusFilter status;

    private LongFilter productId;

    private Boolean distinct;

    public GiftCriteria() {}

    public GiftCriteria(GiftCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.visibility = other.optionalVisibility().map(VisibilityFilter::copy).orElse(null);
        this.level = other.optionalLevel().map(LevelFilter::copy).orElse(null);
        this.stock = other.optionalStock().map(LongFilter::copy).orElse(null);
        this.unlimitedStock = other.optionalUnlimitedStock().map(BooleanFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(GiftStatusFilter::copy).orElse(null);
        this.productId = other.optionalProductId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public GiftCriteria copy() {
        return new GiftCriteria(this);
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

    public GiftStatusFilter getStatus() {
        return status;
    }

    public Optional<GiftStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public GiftStatusFilter status() {
        if (status == null) {
            setStatus(new GiftStatusFilter());
        }
        return status;
    }

    public void setStatus(GiftStatusFilter status) {
        this.status = status;
    }

    public LongFilter getProductId() {
        return productId;
    }

    public Optional<LongFilter> optionalProductId() {
        return Optional.ofNullable(productId);
    }

    public LongFilter productId() {
        if (productId == null) {
            setProductId(new LongFilter());
        }
        return productId;
    }

    public void setProductId(LongFilter productId) {
        this.productId = productId;
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
        final GiftCriteria that = (GiftCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(visibility, that.visibility) &&
            Objects.equals(level, that.level) &&
            Objects.equals(stock, that.stock) &&
            Objects.equals(unlimitedStock, that.unlimitedStock) &&
            Objects.equals(status, that.status) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, visibility, level, stock, unlimitedStock, status, productId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "GiftCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalVisibility().map(f -> "visibility=" + f + ", ").orElse("") +
            optionalLevel().map(f -> "level=" + f + ", ").orElse("") +
            optionalStock().map(f -> "stock=" + f + ", ").orElse("") +
            optionalUnlimitedStock().map(f -> "unlimitedStock=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalProductId().map(f -> "productId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
