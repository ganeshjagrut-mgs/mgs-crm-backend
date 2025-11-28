package com.mgs.service.mapper;

import com.mgs.domain.Role;
import com.mgs.domain.Tenant;
import com.mgs.domain.User;
import com.mgs.domain.UserRole;
import com.mgs.service.dto.RoleDTO;
import com.mgs.service.dto.TenantDTO;
import com.mgs.service.dto.UserDTO;
import com.mgs.service.dto.UserRoleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserRole} and its DTO {@link UserRoleDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserRoleMapper extends EntityMapper<UserRoleDTO, UserRole> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "tenantId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "role", source = "role", qualifiedByName = "roleId")
    UserRoleDTO toDto(UserRole s);

    @Named("tenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TenantDTO toDtoTenantId(Tenant tenant);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("roleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    RoleDTO toDtoRoleId(Role role);
}
