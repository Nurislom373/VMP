package org.khasanof.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.khasanof.domain.enumeration.NotificationStatus;
import org.khasanof.domain.enumeration.NotificationType;

/**
 * A Notification.
 */
@Entity
@Table(name = "notification")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Notification implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "sender", nullable = false)
    private String sender;

    @NotNull
    @Column(name = "recipient", nullable = false)
    private String recipient;

    @Lob
    @Column(name = "message", nullable = false)
    private String message;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private NotificationStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private NotificationType type;

    @Lob
    @Column(name = "link")
    private String link;

    @Lob
    @Column(name = "params")
    private String params;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Notification id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSender() {
        return this.sender;
    }

    public Notification sender(String sender) {
        this.setSender(sender);
        return this;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return this.recipient;
    }

    public Notification recipient(String recipient) {
        this.setRecipient(recipient);
        return this;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return this.message;
    }

    public Notification message(String message) {
        this.setMessage(message);
        return this;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationStatus getStatus() {
        return this.status;
    }

    public Notification status(NotificationStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public NotificationType getType() {
        return this.type;
    }

    public Notification type(NotificationType type) {
        this.setType(type);
        return this;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getLink() {
        return this.link;
    }

    public Notification link(String link) {
        this.setLink(link);
        return this;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getParams() {
        return this.params;
    }

    public Notification params(String params) {
        this.setParams(params);
        return this;
    }

    public void setParams(String params) {
        this.params = params;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Notification)) {
            return false;
        }
        return getId() != null && getId().equals(((Notification) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Notification{" +
            "id=" + getId() +
            ", sender='" + getSender() + "'" +
            ", recipient='" + getRecipient() + "'" +
            ", message='" + getMessage() + "'" +
            ", status='" + getStatus() + "'" +
            ", type='" + getType() + "'" +
            ", link='" + getLink() + "'" +
            ", params='" + getParams() + "'" +
            "}";
    }
}
