package org.khasanof.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.khasanof.domain.Price} entity. This class is used
 * in {@link org.khasanof.web.rest.PriceResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /prices?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PriceCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter price;

    private BooleanFilter isBase;

    private StringFilter sku;

    private LongFilter variantsId;

    private Boolean distinct;

    public PriceCriteria() {}

    public PriceCriteria(PriceCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.price = other.optionalPrice().map(BigDecimalFilter::copy).orElse(null);
        this.isBase = other.optionalIsBase().map(BooleanFilter::copy).orElse(null);
        this.sku = other.optionalSku().map(StringFilter::copy).orElse(null);
        this.variantsId = other.optionalVariantsId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PriceCriteria copy() {
        return new PriceCriteria(this);
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

    public BigDecimalFilter getPrice() {
        return price;
    }

    public Optional<BigDecimalFilter> optionalPrice() {
        return Optional.ofNullable(price);
    }

    public BigDecimalFilter price() {
        if (price == null) {
            setPrice(new BigDecimalFilter());
        }
        return price;
    }

    public void setPrice(BigDecimalFilter price) {
        this.price = price;
    }

    public BooleanFilter getIsBase() {
        return isBase;
    }

    public Optional<BooleanFilter> optionalIsBase() {
        return Optional.ofNullable(isBase);
    }

    public BooleanFilter isBase() {
        if (isBase == null) {
            setIsBase(new BooleanFilter());
        }
        return isBase;
    }

    public void setIsBase(BooleanFilter isBase) {
        this.isBase = isBase;
    }

    public StringFilter getSku() {
        return sku;
    }

    public Optional<StringFilter> optionalSku() {
        return Optional.ofNullable(sku);
    }

    public StringFilter sku() {
        if (sku == null) {
            setSku(new StringFilter());
        }
        return sku;
    }

    public void setSku(StringFilter sku) {
        this.sku = sku;
    }

    public LongFilter getVariantsId() {
        return variantsId;
    }

    public Optional<LongFilter> optionalVariantsId() {
        return Optional.ofNullable(variantsId);
    }

    public LongFilter variantsId() {
        if (variantsId == null) {
            setVariantsId(new LongFilter());
        }
        return variantsId;
    }

    public void setVariantsId(LongFilter variantsId) {
        this.variantsId = variantsId;
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
        final PriceCriteria that = (PriceCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(price, that.price) &&
            Objects.equals(isBase, that.isBase) &&
            Objects.equals(sku, that.sku) &&
            Objects.equals(variantsId, that.variantsId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, isBase, sku, variantsId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PriceCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalPrice().map(f -> "price=" + f + ", ").orElse("") +
            optionalIsBase().map(f -> "isBase=" + f + ", ").orElse("") +
            optionalSku().map(f -> "sku=" + f + ", ").orElse("") +
            optionalVariantsId().map(f -> "variantsId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
