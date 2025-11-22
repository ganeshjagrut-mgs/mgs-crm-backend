package com.crm.service.mapper;

import com.crm.domain.Tenant;
import com.crm.domain.User;
import com.crm.service.dto.TenantDTO;
import com.crm.service.dto.UserDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tenant} and its DTO {@link TenantDTO}.
 */
@Mapper(componentModel = "spring")
public interface TenantMapper extends EntityMapper<TenantDTO, Tenant> {
    @Mapping(target = "users", source = "users", qualifiedByName = "userIdSet")
    TenantDTO toDto(Tenant s);

    @Mapping(target = "removeUsers", ignore = true)
    Tenant toEntity(TenantDTO tenantDTO);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("userIdSet")
    default Set<UserDTO> toDtoUserIdSet(Set<User> user) {
        return user.stream().map(this::toDtoUserId).collect(Collectors.toSet());
    }
}
