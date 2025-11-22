package com.crm.service.mapper;

import com.crm.domain.Address;
import com.crm.domain.City;
import com.crm.domain.Country;
import com.crm.domain.Customer;
import com.crm.domain.State;
import com.crm.domain.Tenant;
import com.crm.service.dto.AddressDTO;
import com.crm.service.dto.CityDTO;
import com.crm.service.dto.CountryDTO;
import com.crm.service.dto.CustomerDTO;
import com.crm.service.dto.StateDTO;
import com.crm.service.dto.TenantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Address} and its DTO {@link AddressDTO}.
 */
@Mapper(componentModel = "spring")
public interface AddressMapper extends EntityMapper<AddressDTO, Address> {
    @Mapping(target = "city", source = "city", qualifiedByName = "cityId")
    @Mapping(target = "state", source = "state", qualifiedByName = "stateId")
    @Mapping(target = "country", source = "country", qualifiedByName = "countryId")
    @Mapping(target = "customer", source = "customer", qualifiedByName = "customerId")
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "tenantId")
    AddressDTO toDto(Address s);

    @Named("cityId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CityDTO toDtoCityId(City city);

    @Named("stateId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StateDTO toDtoStateId(State state);

    @Named("countryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CountryDTO toDtoCountryId(Country country);

    @Named("customerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerDTO toDtoCustomerId(Customer customer);

    @Named("tenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TenantDTO toDtoTenantId(Tenant tenant);
}
