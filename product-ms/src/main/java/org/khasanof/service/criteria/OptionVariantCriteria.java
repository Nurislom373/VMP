package org.khasanof.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.khasanof.domain.enumeration.OptionVariantStatus;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.khasanof.domain.OptionVariant} entity. This class is used
 * in {@link org.khasanof.web.rest.OptionVariantResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /option-variants?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class OptionVariantCriteria implements Serializable, Criteria {

    /**
     * Class for filtering OptionVariantStatus
     */
    public static class OptionVariantStatusFilter extends Filter<OptionVariantStatus> {

        public OptionVariantStatusFilter() {}

        public OptionVariantStatusFilter(OptionVariantStatusFilter filter) {
            super(filter);
        }

        @Override
        public OptionVariantStatusFilter copy() {
            return new OptionVariantStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private OptionVariantStatusFilter status;

    private LongFilter priceId;

    private LongFilter optionId;

    private Boolean distinct;

    public OptionVariantCriteria() {}

    public OptionVariantCriteria(OptionVariantCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(OptionVariantStatusFilter::copy).orElse(null);
        this.priceId = other.optionalPriceId().map(LongFilter::copy).orElse(null);
        this.optionId = other.optionalOptionId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public OptionVariantCriteria copy() {
        return new OptionVariantCriteria(this);
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

    public OptionVariantStatusFilter getStatus() {
        return status;
    }

    public Optional<OptionVariantStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public OptionVariantStatusFilter status() {
        if (status == null) {
            setStatus(new OptionVariantStatusFilter());
        }
        return status;
    }

    public void setStatus(OptionVariantStatusFilter status) {
        this.status = status;
    }

    public LongFilter getPriceId() {
        return priceId;
    }

    public Optional<LongFilter> optionalPriceId() {
        return Optional.ofNullable(priceId);
    }

    public LongFilter priceId() {
        if (priceId == null) {
            setPriceId(new LongFilter());
        }
        return priceId;
    }

    public void setPriceId(LongFilter priceId) {
        this.priceId = priceId;
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
        final OptionVariantCriteria that = (OptionVariantCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(status, that.status) &&
            Objects.equals(priceId, that.priceId) &&
            Objects.equals(optionId, that.optionId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, status, priceId, optionId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OptionVariantCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalPriceId().map(f -> "priceId=" + f + ", ").orElse("") +
            optionalOptionId().map(f -> "optionId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
