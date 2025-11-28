package com.mgs.service.mapper;

import com.mgs.domain.Address;
import com.mgs.domain.Contact;
import com.mgs.domain.Customer;
import com.mgs.domain.Department;
import com.mgs.domain.Tenant;
import com.mgs.service.dto.AddressDTO;
import com.mgs.service.dto.ContactDTO;
import com.mgs.service.dto.CustomerDTO;
import com.mgs.service.dto.DepartmentDTO;
import com.mgs.service.dto.TenantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Customer} and its DTO {@link CustomerDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerMapper extends EntityMapper<CustomerDTO, Customer> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "tenantId")
    @Mapping(target = "department", source = "department", qualifiedByName = "departmentId")
    @Mapping(target = "billingAddress", source = "billingAddress", qualifiedByName = "addressId")
    @Mapping(target = "shippingAddress", source = "shippingAddress", qualifiedByName = "addressId")
    @Mapping(target = "primaryContact", source = "primaryContact", qualifiedByName = "contactId")
    CustomerDTO toDto(Customer s);

    @Named("tenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TenantDTO toDtoTenantId(Tenant tenant);

    @Named("departmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DepartmentDTO toDtoDepartmentId(Department department);

    @Named("addressId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AddressDTO toDtoAddressId(Address address);

    @Named("contactId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContactDTO toDtoContactId(Contact contact);
}
