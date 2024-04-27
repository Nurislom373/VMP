package org.khasanof.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import org.khasanof.domain.enumeration.NotificationStatus;
import org.khasanof.domain.enumeration.NotificationType;

/**
 * A DTO for the {@link org.khasanof.domain.Notification} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class NotificationDTO implements Serializable {

    private Long id;

    @NotNull
    private String sender;

    @NotNull
    private String recipient;

    @Lob
    private String message;

    @NotNull
    private NotificationStatus status;

    @NotNull
    private NotificationType type;

    @Lob
    private String link;

    @Lob
    private String params;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public NotificationStatus getStatus() {
        return status;
    }

    public void setStatus(NotificationStatus status) {
        this.status = status;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NotificationDTO)) {
            return false;
        }

        NotificationDTO notificationDTO = (NotificationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, notificationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NotificationDTO{" +
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
