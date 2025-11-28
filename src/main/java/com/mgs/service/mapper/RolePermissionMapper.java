package com.mgs.service.mapper;

import com.mgs.domain.PermissionModule;
import com.mgs.domain.Role;
import com.mgs.domain.RolePermission;
import com.mgs.service.dto.PermissionModuleDTO;
import com.mgs.service.dto.RoleDTO;
import com.mgs.service.dto.RolePermissionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RolePermission} and its DTO {@link RolePermissionDTO}.
 */
@Mapper(componentModel = "spring")
public interface RolePermissionMapper extends EntityMapper<RolePermissionDTO, RolePermission> {
    @Mapping(target = "role", source = "role", qualifiedByName = "roleId")
    @Mapping(target = "module", source = "module", qualifiedByName = "permissionModuleId")
    RolePermissionDTO toDto(RolePermission s);

    @Named("roleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RoleDTO toDtoRoleId(Role role);

    @Named("permissionModuleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PermissionModuleDTO toDtoPermissionModuleId(PermissionModule permissionModule);
}
