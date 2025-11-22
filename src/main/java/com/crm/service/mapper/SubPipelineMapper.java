package com.crm.service.mapper;

import com.crm.domain.SubPipeline;
import com.crm.service.dto.SubPipelineDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubPipeline} and its DTO {@link SubPipelineDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubPipelineMapper extends EntityMapper<SubPipelineDTO, SubPipeline> {}
