package com.mgs.service.mapper;

import com.mgs.domain.LeadSource;
import com.mgs.domain.Tenant;
import com.mgs.service.dto.LeadSourceDTO;
import com.mgs.service.dto.TenantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link LeadSource} and its DTO {@link LeadSourceDTO}.
 */
@Mapper(componentModel = "spring")
public interface LeadSourceMapper extends EntityMapper<LeadSourceDTO, LeadSource> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "tenantId")
    LeadSourceDTO toDto(LeadSource s);

    @Named("tenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TenantDTO toDtoTenantId(Tenant tenant);
}
