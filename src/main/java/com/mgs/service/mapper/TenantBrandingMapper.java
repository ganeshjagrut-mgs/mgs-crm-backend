package com.mgs.service.mapper;

import com.mgs.domain.Tenant;
import com.mgs.domain.TenantBranding;
import com.mgs.service.dto.TenantBrandingDTO;
import com.mgs.service.dto.TenantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TenantBranding} and its DTO {@link TenantBrandingDTO}.
 */
@Mapper(componentModel = "spring")
public interface TenantBrandingMapper extends EntityMapper<TenantBrandingDTO, TenantBranding> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "tenantId")
    TenantBrandingDTO toDto(TenantBranding s);

    @Named("tenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TenantDTO toDtoTenantId(Tenant tenant);
}
