package com.mgs.service.mapper;

import com.mgs.domain.Contact;
import com.mgs.domain.Customer;
import com.mgs.domain.Tenant;
import com.mgs.domain.Ticket;
import com.mgs.domain.User;
import com.mgs.service.dto.ContactDTO;
import com.mgs.service.dto.CustomerDTO;
import com.mgs.service.dto.TenantDTO;
import com.mgs.service.dto.TicketDTO;
import com.mgs.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ticket} and its DTO {@link TicketDTO}.
 */
@Mapper(componentModel = "spring")
public interface TicketMapper extends EntityMapper<TicketDTO, Ticket> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "tenantId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    @Mapping(target = "contact", source = "contact", qualifiedByName = "contactId")
    @Mapping(target = "assignedTo", source = "assignedTo", qualifiedByName = "userId")
    TicketDTO toDto(Ticket s);

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

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
