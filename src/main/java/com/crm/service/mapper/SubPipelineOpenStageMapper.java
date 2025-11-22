package com.crm.service.mapper;

import com.crm.domain.MasterStaticType;
import com.crm.domain.SubPipeline;
import com.crm.domain.SubPipelineOpenStage;
import com.crm.service.dto.MasterStaticTypeDTO;
import com.crm.service.dto.SubPipelineDTO;
import com.crm.service.dto.SubPipelineOpenStageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SubPipelineOpenStage} and its DTO {@link SubPipelineOpenStageDTO}.
 */
@Mapper(componentModel = "spring")
public interface SubPipelineOpenStageMapper extends EntityMapper<SubPipelineOpenStageDTO, SubPipelineOpenStage> {
    @Mapping(target = "stage", source = "stage", qualifiedByName = "masterStaticTypeId")
    @Mapping(target = "subPipeline", source = "subPipeline", qualifiedByName = "subPipelineId")
    SubPipelineOpenStageDTO toDto(SubPipelineOpenStage s);

    @Named("masterStaticTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterStaticTypeDTO toDtoMasterStaticTypeId(MasterStaticType masterStaticType);

    @Named("subPipelineId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SubPipelineDTO toDtoSubPipelineId(SubPipeline subPipeline);
}
