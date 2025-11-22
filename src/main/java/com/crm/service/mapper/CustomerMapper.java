package com.crm.service.mapper;

import com.crm.domain.Customer;
import com.crm.domain.CustomerCompany;
import com.crm.domain.Department;
import com.crm.domain.MasterCategory;
import com.crm.domain.MasterStaticType;
import com.crm.domain.Tenant;
import com.crm.domain.User;
import com.crm.service.dto.CustomerCompanyDTO;
import com.crm.service.dto.CustomerDTO;
import com.crm.service.dto.DepartmentDTO;
import com.crm.service.dto.MasterCategoryDTO;
import com.crm.service.dto.MasterStaticTypeDTO;
import com.crm.service.dto.TenantDTO;
import com.crm.service.dto.UserDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {
    @Mapping(target = "company", source = "company", qualifiedByName = "customerCompanyId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "customerType", source = "customerType", qualifiedByName = "masterStaticTypeId")
    @Mapping(target = "customerStatus", source = "customerStatus", qualifiedByName = "masterStaticTypeId")
    @Mapping(target = "ownershipType", source = "ownershipType", qualifiedByName = "masterStaticTypeId")
    @Mapping(target = "industryType", source = "industryType", qualifiedByName = "masterStaticTypeId")
    @Mapping(target = "customerCategory", source = "customerCategory", qualifiedByName = "masterStaticTypeId")
    @Mapping(target = "paymentTerms", source = "paymentTerms", qualifiedByName = "masterStaticTypeId")
    @Mapping(target = "invoiceFrequency", source = "invoiceFrequency", qualifiedByName = "masterStaticTypeId")
    @Mapping(target = "gstTreatment", source = "gstTreatment", qualifiedByName = "masterStaticTypeId")
    @Mapping(target = "outstandingPerson", source = "outstandingPerson", qualifiedByName = "userId")
    @Mapping(target = "department", source = "department", qualifiedByName = "departmentId")
    @Mapping(target = "tenat", source = "tenat", qualifiedByName = "tenantId")
    @Mapping(target = "masterCategories", source = "masterCategories", qualifiedByName = "masterCategoryIdSet")
    CustomerDTO toDto(Customer s);

    @Mapping(target = "removeMasterCategories", ignore = true)
    Customer toEntity(CustomerDTO customerDTO);

    @Named("customerCompanyId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerCompanyDTO toDtoCustomerCompanyId(CustomerCompany customerCompany);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("masterStaticTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterStaticTypeDTO toDtoMasterStaticTypeId(MasterStaticType masterStaticType);

    @Named("departmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DepartmentDTO toDtoDepartmentId(Department department);

    @Named("tenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TenantDTO toDtoTenantId(Tenant tenant);

    @Named("masterCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterCategoryDTO toDtoMasterCategoryId(MasterCategory masterCategory);

    @Named("masterCategoryIdSet")
    default Set<MasterCategoryDTO> toDtoMasterCategoryIdSet(Set<MasterCategory> masterCategory) {
        return masterCategory.stream().map(this::toDtoMasterCategoryId).collect(Collectors.toSet());
    }
}
