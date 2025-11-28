package com.mgs.service.mapper;

import com.mgs.domain.Event;
import com.mgs.domain.EventTaskAssignment;
import com.mgs.domain.Task;
import com.mgs.domain.User;
import com.mgs.service.dto.EventDTO;
import com.mgs.service.dto.EventTaskAssignmentDTO;
import com.mgs.service.dto.TaskDTO;
import com.mgs.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EventTaskAssignment} and its DTO {@link EventTaskAssignmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface EventTaskAssignmentMapper extends EntityMapper<EventTaskAssignmentDTO, EventTaskAssignment> {
    @Mapping(target = "event", source = "event", qualifiedByName = "eventId")
    @Mapping(target = "task", source = "task", qualifiedByName = "taskId")
    @Mapping(target = "assignedTo", source = "assignedTo", qualifiedByName = "userId")
    EventTaskAssignmentDTO toDto(EventTaskAssignment s);

    @Named("eventId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EventDTO toDtoEventId(Event event);

    @Named("taskId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TaskDTO toDtoTaskId(Task task);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
