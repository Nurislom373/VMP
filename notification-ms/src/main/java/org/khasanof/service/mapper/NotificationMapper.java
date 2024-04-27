package org.khasanof.service.mapper;

import org.khasanof.domain.Notification;
import org.khasanof.service.dto.NotificationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Notification} and its DTO {@link NotificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface NotificationMapper extends EntityMapper<NotificationDTO, Notification> {}
