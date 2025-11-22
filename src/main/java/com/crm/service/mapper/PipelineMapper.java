package com.crm.service.mapper;

import com.crm.domain.Customer;
import com.crm.domain.MasterStaticType;
import com.crm.domain.Pipeline;
import com.crm.domain.SubPipeline;
import com.crm.domain.User;
import com.crm.service.dto.CustomerDTO;
import com.crm.service.dto.MasterStaticTypeDTO;
import com.crm.service.dto.PipelineDTO;
import com.crm.service.dto.SubPipelineDTO;
import com.crm.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Pipeline} and its DTO {@link PipelineDTO}.
 */
@Mapper(componentModel = "spring")
public interface PipelineMapper extends EntityMapper<PipelineDTO, Pipeline> {
    @Mapping(target = "pipelineOwner", source = "pipelineOwner", qualifiedByName = "userId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    @Mapping(target = "stageOfPipeline", source = "stageOfPipeline", qualifiedByName = "masterStaticTypeId")
    @Mapping(target = "subPipeline", source = "subPipeline", qualifiedByName = "subPipelineId")
    PipelineDTO toDto(Pipeline s);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("customerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerDTO toDtoCustomerId(Customer customer);

    @Named("masterStaticTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterStaticTypeDTO toDtoMasterStaticTypeId(MasterStaticType masterStaticType);

    @Named("subPipelineId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SubPipelineDTO toDtoSubPipelineId(SubPipeline subPipeline);
}
