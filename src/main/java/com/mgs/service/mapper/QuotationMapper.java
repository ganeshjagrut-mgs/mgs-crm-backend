package com.mgs.service.mapper;

import com.mgs.domain.Contact;
import com.mgs.domain.Customer;
import com.mgs.domain.Lead;
import com.mgs.domain.Quotation;
import com.mgs.domain.Tenant;
import com.mgs.domain.User;
import com.mgs.service.dto.ContactDTO;
import com.mgs.service.dto.CustomerDTO;
import com.mgs.service.dto.LeadDTO;
import com.mgs.service.dto.QuotationDTO;
import com.mgs.service.dto.TenantDTO;
import com.mgs.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Quotation} and its DTO {@link QuotationDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuotationMapper extends EntityMapper<QuotationDTO, Quotation> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "tenantId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    @Mapping(target = "contact", source = "contact", qualifiedByName = "contactId")
    @Mapping(target = "lead", source = "lead", qualifiedByName = "leadId")
    @Mapping(target = "createdByUser", source = "createdByUser", qualifiedByName = "userId")
    QuotationDTO toDto(Quotation s);

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

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
