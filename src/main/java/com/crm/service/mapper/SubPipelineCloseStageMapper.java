package com.crm.service.mapper;

import com.crm.domain.MasterStaticType;
import com.crm.domain.SubPipeline;
import com.crm.domain.SubPipelineCloseStage;
import com.crm.service.dto.MasterStaticTypeDTO;
import com.crm.service.dto.SubPipelineCloseStageDTO;
import com.crm.service.dto.SubPipelineDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubPipelineCloseStage} and its DTO {@link SubPipelineCloseStageDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubPipelineCloseStageMapper extends EntityMapper<SubPipelineCloseStageDTO, SubPipelineCloseStage> {
    @Mapping(target = "stage", source = "stage", qualifiedByName = "masterStaticTypeId")
    @Mapping(target = "subPipeline", source = "subPipeline", qualifiedByName = "subPipelineId")
    SubPipelineCloseStageDTO toDto(SubPipelineCloseStage s);

    @Named("masterStaticTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterStaticTypeDTO toDtoMasterStaticTypeId(MasterStaticType masterStaticType);

    @Named("subPipelineId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SubPipelineDTO toDtoSubPipelineId(SubPipeline subPipeline);
}
