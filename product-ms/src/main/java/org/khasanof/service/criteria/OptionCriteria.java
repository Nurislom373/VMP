package org.khasanof.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.khasanof.domain.enumeration.OptionStatus;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.khasanof.domain.Option} entity. This class is used
 * in {@link org.khasanof.web.rest.OptionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /options?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OptionCriteria implements Serializable, Criteria {

    /**
     * Class for filtering OptionStatus
     */
    public static class OptionStatusFilter extends Filter<OptionStatus> {

        public OptionStatusFilter() {}

        public OptionStatusFilter(OptionStatusFilter filter) {
            super(filter);
        }

        @Override
        public OptionStatusFilter copy() {
            return new OptionStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private OptionStatusFilter status;

    private LongFilter variantsId;

    private LongFilter productId;

    private Boolean distinct;

    public OptionCriteria() {}

    public OptionCriteria(OptionCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(OptionStatusFilter::copy).orElse(null);
        this.variantsId = other.optionalVariantsId().map(LongFilter::copy).orElse(null);
        this.productId = other.optionalProductId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public OptionCriteria copy() {
        return new OptionCriteria(this);
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

    public OptionStatusFilter getStatus() {
        return status;
    }

    public Optional<OptionStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public OptionStatusFilter status() {
        if (status == null) {
            setStatus(new OptionStatusFilter());
        }
        return status;
    }

    public void setStatus(OptionStatusFilter status) {
        this.status = status;
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
        final OptionCriteria that = (OptionCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(status, that.status) &&
            Objects.equals(variantsId, that.variantsId) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, variantsId, productId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OptionCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalVariantsId().map(f -> "variantsId=" + f + ", ").orElse("") +
            optionalProductId().map(f -> "productId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
