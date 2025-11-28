package com.mgs.service.mapper;

import com.mgs.domain.Event;
import com.mgs.domain.EventNotification;
import com.mgs.domain.User;
import com.mgs.service.dto.EventDTO;
import com.mgs.service.dto.EventNotificationDTO;
import com.mgs.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventNotification} and its DTO {@link EventNotificationDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventNotificationMapper extends EntityMapper<EventNotificationDTO, EventNotification> {
    @Mapping(target = "event", source = "event", qualifiedByName = "eventId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    EventNotificationDTO toDto(EventNotification s);

    @Named("eventId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDTO toDtoEventId(Event event);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
