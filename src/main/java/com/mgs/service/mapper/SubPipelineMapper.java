package com.mgs.service.mapper;

import com.mgs.domain.Pipeline;
import com.mgs.domain.SubPipeline;
import com.mgs.domain.Tenant;
import com.mgs.service.dto.PipelineDTO;
import com.mgs.service.dto.SubPipelineDTO;
import com.mgs.service.dto.TenantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubPipeline} and its DTO {@link SubPipelineDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubPipelineMapper extends EntityMapper<SubPipelineDTO, SubPipeline> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "tenantId")
    @Mapping(target = "pipeline", source = "pipeline", qualifiedByName = "pipelineId")
    SubPipelineDTO toDto(SubPipeline s);

    @Named("tenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TenantDTO toDtoTenantId(Tenant tenant);

    @Named("pipelineId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PipelineDTO toDtoPipelineId(Pipeline pipeline);
}
