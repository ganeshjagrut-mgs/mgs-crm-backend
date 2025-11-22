package com.crm.service.mapper;

import com.crm.domain.Pipeline;
import com.crm.domain.PipelineTag;
import com.crm.service.dto.PipelineDTO;
import com.crm.service.dto.PipelineTagDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PipelineTag} and its DTO {@link PipelineTagDTO}.
 */
@Mapper(componentModel = "spring")
public interface PipelineTagMapper extends EntityMapper<PipelineTagDTO, PipelineTag> {
    @Mapping(target = "pipeline", source = "pipeline", qualifiedByName = "pipelineId")
    PipelineTagDTO toDto(PipelineTag s);

    @Named("pipelineId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PipelineDTO toDtoPipelineId(Pipeline pipeline);
}
