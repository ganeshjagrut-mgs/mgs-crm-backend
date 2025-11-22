package com.crm.service.mapper;

import com.crm.domain.TaskAudit;
import com.crm.service.dto.TaskAuditDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TaskAudit} and its DTO {@link TaskAuditDTO}.
 */
@Mapper(componentModel = "spring")
public interface TaskAuditMapper extends EntityMapper<TaskAuditDTO, TaskAudit> {}
