package com.mgs.service.mapper;

import com.mgs.domain.Contact;
import com.mgs.domain.Customer;
import com.mgs.domain.Deal;
import com.mgs.domain.Lead;
import com.mgs.domain.Tenant;
import com.mgs.service.dto.ContactDTO;
import com.mgs.service.dto.CustomerDTO;
import com.mgs.service.dto.DealDTO;
import com.mgs.service.dto.LeadDTO;
import com.mgs.service.dto.TenantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Deal} and its DTO {@link DealDTO}.
 */
@Mapper(componentModel = "spring")
public interface DealMapper extends EntityMapper<DealDTO, Deal> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "tenantId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    @Mapping(target = "contact", source = "contact", qualifiedByName = "contactId")
    @Mapping(target = "lead", source = "lead", qualifiedByName = "leadId")
    DealDTO toDto(Deal s);

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

    @Named("leadId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LeadDTO toDtoLeadId(Lead lead);
}
