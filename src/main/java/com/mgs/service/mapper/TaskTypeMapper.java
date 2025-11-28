package com.mgs.service.mapper;

import com.mgs.domain.TaskType;
import com.mgs.domain.Tenant;
import com.mgs.service.dto.TaskTypeDTO;
import com.mgs.service.dto.TenantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TaskType} and its DTO {@link TaskTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface TaskTypeMapper extends EntityMapper<TaskTypeDTO, TaskType> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "tenantId")
    TaskTypeDTO toDto(TaskType s);

    @Named("tenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TenantDTO toDtoTenantId(Tenant tenant);
}
