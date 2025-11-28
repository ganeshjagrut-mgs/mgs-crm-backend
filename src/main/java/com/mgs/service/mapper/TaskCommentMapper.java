package com.mgs.service.mapper;

import com.mgs.domain.Task;
import com.mgs.domain.TaskComment;
import com.mgs.domain.User;
import com.mgs.service.dto.TaskCommentDTO;
import com.mgs.service.dto.TaskDTO;
import com.mgs.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TaskComment} and its DTO {@link TaskCommentDTO}.
 */
@Mapper(componentModel = "spring")
public interface TaskCommentMapper extends EntityMapper<TaskCommentDTO, TaskComment> {
    @Mapping(target = "task", source = "task", qualifiedByName = "taskId")
    @Mapping(target = "createdByUser", source = "createdByUser", qualifiedByName = "userId")
    TaskCommentDTO toDto(TaskComment s);

    @Named("taskId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TaskDTO toDtoTaskId(Task task);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
