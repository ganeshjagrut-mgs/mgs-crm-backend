package com.crm.service.mapper;

import com.crm.domain.Customer;
import com.crm.domain.MasterStaticType;
import com.crm.domain.Quotation;
import com.crm.domain.User;
import com.crm.service.dto.CustomerDTO;
import com.crm.service.dto.MasterStaticTypeDTO;
import com.crm.service.dto.QuotationDTO;
import com.crm.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Quotation} and its DTO {@link QuotationDTO}.
 */
@Mapper(componentModel = "spring")
public interface QuotationMapper extends EntityMapper<QuotationDTO, Quotation> {
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    @Mapping(target = "paymentTerm", source = "paymentTerm", qualifiedByName = "masterStaticTypeId")
    @Mapping(target = "quotationStatus", source = "quotationStatus", qualifiedByName = "masterStaticTypeId")
    QuotationDTO toDto(Quotation s);

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
