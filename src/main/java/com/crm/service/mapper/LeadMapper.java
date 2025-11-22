package com.crm.service.mapper;

import com.crm.domain.Customer;
import com.crm.domain.Lead;
import com.crm.domain.MasterStaticType;
import com.crm.domain.User;
import com.crm.service.dto.CustomerDTO;
import com.crm.service.dto.LeadDTO;
import com.crm.service.dto.MasterStaticTypeDTO;
import com.crm.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Lead} and its DTO {@link LeadDTO}.
 */
@Mapper(componentModel = "spring")
public interface LeadMapper extends EntityMapper<LeadDTO, Lead> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    @Mapping(target = "leadSource", source = "leadSource", qualifiedByName = "masterStaticTypeId")
    @Mapping(target = "industryType", source = "industryType", qualifiedByName = "masterStaticTypeId")
    @Mapping(target = "leadStatus", source = "leadStatus", qualifiedByName = "masterStaticTypeId")
    LeadDTO toDto(Lead s);

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
}
