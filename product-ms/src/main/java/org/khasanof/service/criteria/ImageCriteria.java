package org.khasanof.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.khasanof.domain.enumeration.ImageStatus;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.khasanof.domain.Image} entity. This class is used
 * in {@link org.khasanof.web.rest.ImageResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /images?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ImageCriteria implements Serializable, Criteria {

    /**
     * Class for filtering ImageStatus
     */
    public static class ImageStatusFilter extends Filter<ImageStatus> {

        public ImageStatusFilter() {}

        public ImageStatusFilter(ImageStatusFilter filter) {
            super(filter);
        }

        @Override
        public ImageStatusFilter copy() {
            return new ImageStatusFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter imageKey;

    private ImageStatusFilter status;

    private LongFilter productId;

    private Boolean distinct;

    public ImageCriteria() {}

    public ImageCriteria(ImageCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.imageKey = other.optionalImageKey().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(ImageStatusFilter::copy).orElse(null);
        this.productId = other.optionalProductId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public ImageCriteria copy() {
        return new ImageCriteria(this);
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

    public StringFilter getImageKey() {
        return imageKey;
    }

    public Optional<StringFilter> optionalImageKey() {
        return Optional.ofNullable(imageKey);
    }

    public StringFilter imageKey() {
        if (imageKey == null) {
            setImageKey(new StringFilter());
        }
        return imageKey;
    }

    public void setImageKey(StringFilter imageKey) {
        this.imageKey = imageKey;
    }

    public ImageStatusFilter getStatus() {
        return status;
    }

    public Optional<ImageStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public ImageStatusFilter status() {
        if (status == null) {
            setStatus(new ImageStatusFilter());
        }
        return status;
    }

    public void setStatus(ImageStatusFilter status) {
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
        final ImageCriteria that = (ImageCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(imageKey, that.imageKey) &&
            Objects.equals(status, that.status) &&
            Objects.equals(productId, that.productId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, imageKey, status, productId, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ImageCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalImageKey().map(f -> "imageKey=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalProductId().map(f -> "productId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
