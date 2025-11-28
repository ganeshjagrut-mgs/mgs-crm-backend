package com.mgs.service.mapper;

import com.mgs.domain.Tenant;
import com.mgs.domain.TenantEncryptionKey;
import com.mgs.service.dto.TenantDTO;
import com.mgs.service.dto.TenantEncryptionKeyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TenantEncryptionKey} and its DTO {@link TenantEncryptionKeyDTO}.
 */
@Mapper(componentModel = "spring")
public interface TenantEncryptionKeyMapper extends EntityMapper<TenantEncryptionKeyDTO, TenantEncryptionKey> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "tenantId")
    TenantEncryptionKeyDTO toDto(TenantEncryptionKey s);

    @Named("tenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TenantDTO toDtoTenantId(Tenant tenant);
}
