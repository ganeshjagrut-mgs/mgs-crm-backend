package com.crm.service.mapper;

import com.crm.domain.Customer;
import com.crm.domain.MasterStaticType;
import com.crm.domain.Pipeline;
import com.crm.domain.Task;
import com.crm.domain.User;
import com.crm.service.dto.CustomerDTO;
import com.crm.service.dto.MasterStaticTypeDTO;
import com.crm.service.dto.PipelineDTO;
import com.crm.service.dto.TaskDTO;
import com.crm.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Task} and its DTO {@link TaskDTO}.
 */
@Mapper(componentModel = "spring")
public interface TaskMapper extends EntityMapper<TaskDTO, Task> {
    @Mapping(target = "taskOwner", source = "taskOwner", qualifiedByName = "userId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    @Mapping(target = "relatedTo", source = "relatedTo", qualifiedByName = "masterStaticTypeId")
    @Mapping(target = "pipeline", source = "pipeline", qualifiedByName = "pipelineId")
    TaskDTO toDto(Task s);

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

    @Named("pipelineId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PipelineDTO toDtoPipelineId(Pipeline pipeline);
}
