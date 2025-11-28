package com.mgs.service.mapper;

import com.mgs.domain.Complaint;
import com.mgs.domain.ComplaintCategory;
import com.mgs.domain.Contact;
import com.mgs.domain.Customer;
import com.mgs.domain.Department;
import com.mgs.domain.Pipeline;
import com.mgs.domain.SubPipeline;
import com.mgs.domain.Tenant;
import com.mgs.domain.User;
import com.mgs.service.dto.ComplaintCategoryDTO;
import com.mgs.service.dto.ComplaintDTO;
import com.mgs.service.dto.ContactDTO;
import com.mgs.service.dto.CustomerDTO;
import com.mgs.service.dto.DepartmentDTO;
import com.mgs.service.dto.PipelineDTO;
import com.mgs.service.dto.SubPipelineDTO;
import com.mgs.service.dto.TenantDTO;
import com.mgs.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Complaint} and its DTO {@link ComplaintDTO}.
 */
@Mapper(componentModel = "spring")
public interface ComplaintMapper extends EntityMapper<ComplaintDTO, Complaint> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "tenantId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    @Mapping(target = "contact", source = "contact", qualifiedByName = "contactId")
    @Mapping(target = "category", source = "category", qualifiedByName = "complaintCategoryId")
    @Mapping(target = "pipeline", source = "pipeline", qualifiedByName = "pipelineId")
    @Mapping(target = "stage", source = "stage", qualifiedByName = "subPipelineId")
    @Mapping(target = "assignedDepartment", source = "assignedDepartment", qualifiedByName = "departmentId")
    @Mapping(target = "assignedUser", source = "assignedUser", qualifiedByName = "userId")
    ComplaintDTO toDto(Complaint s);

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

    @Named("complaintCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ComplaintCategoryDTO toDtoComplaintCategoryId(ComplaintCategory complaintCategory);

    @Named("pipelineId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PipelineDTO toDtoPipelineId(Pipeline pipeline);

    @Named("subPipelineId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SubPipelineDTO toDtoSubPipelineId(SubPipeline subPipeline);

    @Named("departmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DepartmentDTO toDtoDepartmentId(Department department);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
