package com.mgs.service.mapper;

import com.mgs.domain.Tenant;
import com.mgs.domain.User;
import com.mgs.domain.UserHierarchy;
import com.mgs.service.dto.TenantDTO;
import com.mgs.service.dto.UserDTO;
import com.mgs.service.dto.UserHierarchyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserHierarchy} and its DTO {@link UserHierarchyDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserHierarchyMapper extends EntityMapper<UserHierarchyDTO, UserHierarchy> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "tenantId")
    @Mapping(target = "parentUser", source = "parentUser", qualifiedByName = "userId")
    @Mapping(target = "childUser", source = "childUser", qualifiedByName = "userId")
    UserHierarchyDTO toDto(UserHierarchy s);

    @Named("tenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TenantDTO toDtoTenantId(Tenant tenant);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
