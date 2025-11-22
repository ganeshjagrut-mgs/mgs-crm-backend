package com.crm.service.mapper;

import com.crm.domain.PipelineAudit;
import com.crm.service.dto.PipelineAuditDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PipelineAudit} and its DTO {@link PipelineAuditDTO}.
 */
@Mapper(componentModel = "spring")
public interface PipelineAuditMapper extends EntityMapper<PipelineAuditDTO, PipelineAudit> {}
