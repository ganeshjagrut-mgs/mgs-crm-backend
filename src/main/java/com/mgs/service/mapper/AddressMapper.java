package com.mgs.service.mapper;

import com.mgs.domain.Address;
import com.mgs.domain.Tenant;
import com.mgs.service.dto.AddressDTO;
import com.mgs.service.dto.TenantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Address} and its DTO {@link AddressDTO}.
 */
@Mapper(componentModel = "spring")
public interface AddressMapper extends EntityMapper<AddressDTO, Address> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "tenantId")
    AddressDTO toDto(Address s);

    @Named("tenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TenantDTO toDtoTenantId(Tenant tenant);
}
