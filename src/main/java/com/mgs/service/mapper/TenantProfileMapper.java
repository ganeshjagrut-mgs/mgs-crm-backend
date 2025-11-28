package com.mgs.service.mapper;

import com.mgs.domain.Address;
import com.mgs.domain.Tenant;
import com.mgs.domain.TenantProfile;
import com.mgs.service.dto.AddressDTO;
import com.mgs.service.dto.TenantDTO;
import com.mgs.service.dto.TenantProfileDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TenantProfile} and its DTO {@link TenantProfileDTO}.
 */
@Mapper(componentModel = "spring")
public interface TenantProfileMapper extends EntityMapper<TenantProfileDTO, TenantProfile> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "tenantId")
    @Mapping(target = "address", source = "address", qualifiedByName = "addressId")
    TenantProfileDTO toDto(TenantProfile s);

    @Named("tenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TenantDTO toDtoTenantId(Tenant tenant);

    @Named("addressId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    AddressDTO toDtoAddressId(Address address);
}
