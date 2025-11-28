package com.mgs.service.mapper;

import com.mgs.domain.AuditLog;
import com.mgs.domain.Tenant;
import com.mgs.domain.User;
import com.mgs.service.dto.AuditLogDTO;
import com.mgs.service.dto.TenantDTO;
import com.mgs.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AuditLog} and its DTO {@link AuditLogDTO}.
 */
@Mapper(componentModel = "spring")
public interface AuditLogMapper extends EntityMapper<AuditLogDTO, AuditLog> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "tenantId")
    @Mapping(target = "performedBy", source = "performedBy", qualifiedByName = "userId")
    AuditLogDTO toDto(AuditLog s);

    @Named("tenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TenantDTO toDtoTenantId(Tenant tenant);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
