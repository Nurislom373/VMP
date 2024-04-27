package org.khasanof.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.khasanof.domain.enumeration.NotificationStatus;
import org.khasanof.domain.enumeration.NotificationType;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link org.khasanof.domain.Notification} entity. This class is used
 * in {@link org.khasanof.web.rest.NotificationResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /notifications?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationCriteria implements Serializable, Criteria {

    /**
     * Class for filtering NotificationStatus
     */
    public static class NotificationStatusFilter extends Filter<NotificationStatus> {

        public NotificationStatusFilter() {}

        public NotificationStatusFilter(NotificationStatusFilter filter) {
            super(filter);
        }

        @Override
        public NotificationStatusFilter copy() {
            return new NotificationStatusFilter(this);
        }
    }

    /**
     * Class for filtering NotificationType
     */
    public static class NotificationTypeFilter extends Filter<NotificationType> {

        public NotificationTypeFilter() {}

        public NotificationTypeFilter(NotificationTypeFilter filter) {
            super(filter);
        }

        @Override
        public NotificationTypeFilter copy() {
            return new NotificationTypeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter sender;

    private StringFilter recipient;

    private NotificationStatusFilter status;

    private NotificationTypeFilter type;

    private Boolean distinct;

    public NotificationCriteria() {}

    public NotificationCriteria(NotificationCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.sender = other.optionalSender().map(StringFilter::copy).orElse(null);
        this.recipient = other.optionalRecipient().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(NotificationStatusFilter::copy).orElse(null);
        this.type = other.optionalType().map(NotificationTypeFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public NotificationCriteria copy() {
        return new NotificationCriteria(this);
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

    public StringFilter getSender() {
        return sender;
    }

    public Optional<StringFilter> optionalSender() {
        return Optional.ofNullable(sender);
    }

    public StringFilter sender() {
        if (sender == null) {
            setSender(new StringFilter());
        }
        return sender;
    }

    public void setSender(StringFilter sender) {
        this.sender = sender;
    }

    public StringFilter getRecipient() {
        return recipient;
    }

    public Optional<StringFilter> optionalRecipient() {
        return Optional.ofNullable(recipient);
    }

    public StringFilter recipient() {
        if (recipient == null) {
            setRecipient(new StringFilter());
        }
        return recipient;
    }

    public void setRecipient(StringFilter recipient) {
        this.recipient = recipient;
    }

    public NotificationStatusFilter getStatus() {
        return status;
    }

    public Optional<NotificationStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public NotificationStatusFilter status() {
        if (status == null) {
            setStatus(new NotificationStatusFilter());
        }
        return status;
    }

    public void setStatus(NotificationStatusFilter status) {
        this.status = status;
    }

    public NotificationTypeFilter getType() {
        return type;
    }

    public Optional<NotificationTypeFilter> optionalType() {
        return Optional.ofNullable(type);
    }

    public NotificationTypeFilter type() {
        if (type == null) {
            setType(new NotificationTypeFilter());
        }
        return type;
    }

    public void setType(NotificationTypeFilter type) {
        this.type = type;
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
        final NotificationCriteria that = (NotificationCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(sender, that.sender) &&
            Objects.equals(recipient, that.recipient) &&
            Objects.equals(status, that.status) &&
            Objects.equals(type, that.type) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, sender, recipient, status, type, distinct);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSender().map(f -> "sender=" + f + ", ").orElse("") +
            optionalRecipient().map(f -> "recipient=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalType().map(f -> "type=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
