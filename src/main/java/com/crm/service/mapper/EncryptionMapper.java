package com.crm.service.mapper;

import com.crm.domain.Encryption;
import com.crm.domain.Tenant;
import com.crm.service.dto.EncryptionDTO;
import com.crm.service.dto.TenantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Encryption} and its DTO {@link EncryptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface EncryptionMapper extends EntityMapper<EncryptionDTO, Encryption> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "tenantId")
    EncryptionDTO toDto(Encryption s);

    @Named("tenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TenantDTO toDtoTenantId(Tenant tenant);
}
