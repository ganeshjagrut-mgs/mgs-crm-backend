package com.mgs.service.mapper;

import com.mgs.domain.Role;
import com.mgs.domain.Tenant;
import com.mgs.service.dto.RoleDTO;
import com.mgs.service.dto.TenantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Role} and its DTO {@link RoleDTO}.
 */
@Mapper(componentModel = "spring")
public interface RoleMapper extends EntityMapper<RoleDTO, Role> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "tenantId")
    RoleDTO toDto(Role s);

    @Named("tenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TenantDTO toDtoTenantId(Tenant tenant);
}
