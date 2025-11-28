package com.mgs.service.mapper;

import com.mgs.domain.Pipeline;
import com.mgs.domain.Tenant;
import com.mgs.service.dto.PipelineDTO;
import com.mgs.service.dto.TenantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pipeline} and its DTO {@link PipelineDTO}.
 */
@Mapper(componentModel = "spring")
public interface PipelineMapper extends EntityMapper<PipelineDTO, Pipeline> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "tenantId")
    PipelineDTO toDto(Pipeline s);

    @Named("tenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TenantDTO toDtoTenantId(Tenant tenant);
}
