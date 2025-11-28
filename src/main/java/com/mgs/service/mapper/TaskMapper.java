package com.mgs.service.mapper;

import com.mgs.domain.Task;
import com.mgs.domain.TaskType;
import com.mgs.domain.Tenant;
import com.mgs.domain.User;
import com.mgs.service.dto.TaskDTO;
import com.mgs.service.dto.TaskTypeDTO;
import com.mgs.service.dto.TenantDTO;
import com.mgs.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Task} and its DTO {@link TaskDTO}.
 */
@Mapper(componentModel = "spring")
public interface TaskMapper extends EntityMapper<TaskDTO, Task> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "tenantId")
    @Mapping(target = "taskType", source = "taskType", qualifiedByName = "taskTypeId")
    @Mapping(target = "assignedUser", source = "assignedUser", qualifiedByName = "userId")
    @Mapping(target = "createdByUser", source = "createdByUser", qualifiedByName = "userId")
    TaskDTO toDto(Task s);

    @Named("tenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TenantDTO toDtoTenantId(Tenant tenant);

    @Named("taskTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TaskTypeDTO toDtoTaskTypeId(TaskType taskType);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
