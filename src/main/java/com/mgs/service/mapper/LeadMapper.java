package com.mgs.service.mapper;

import com.mgs.domain.Contact;
import com.mgs.domain.Customer;
import com.mgs.domain.Lead;
import com.mgs.domain.LeadSource;
import com.mgs.domain.Pipeline;
import com.mgs.domain.SubPipeline;
import com.mgs.domain.Tenant;
import com.mgs.domain.User;
import com.mgs.service.dto.ContactDTO;
import com.mgs.service.dto.CustomerDTO;
import com.mgs.service.dto.LeadDTO;
import com.mgs.service.dto.LeadSourceDTO;
import com.mgs.service.dto.PipelineDTO;
import com.mgs.service.dto.SubPipelineDTO;
import com.mgs.service.dto.TenantDTO;
import com.mgs.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Lead} and its DTO {@link LeadDTO}.
 */
@Mapper(componentModel = "spring")
public interface LeadMapper extends EntityMapper<LeadDTO, Lead> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "tenantId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    @Mapping(target = "contact", source = "contact", qualifiedByName = "contactId")
    @Mapping(target = "source", source = "source", qualifiedByName = "leadSourceId")
    @Mapping(target = "pipeline", source = "pipeline", qualifiedByName = "pipelineId")
    @Mapping(target = "stage", source = "stage", qualifiedByName = "subPipelineId")
    @Mapping(target = "ownerUser", source = "ownerUser", qualifiedByName = "userId")
    LeadDTO toDto(Lead s);

    @Named("tenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TenantDTO toDtoTenantId(Tenant tenant);

    @Named("customerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerDTO toDtoCustomerId(Customer customer);

    @Named("contactId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContactDTO toDtoContactId(Contact contact);

    @Named("leadSourceId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LeadSourceDTO toDtoLeadSourceId(LeadSource leadSource);

    @Named("pipelineId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PipelineDTO toDtoPipelineId(Pipeline pipeline);

    @Named("subPipelineId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SubPipelineDTO toDtoSubPipelineId(SubPipeline subPipeline);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
