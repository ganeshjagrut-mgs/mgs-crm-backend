package com.mgs.service.mapper;

import com.mgs.domain.ReportTemplate;
import com.mgs.domain.Tenant;
import com.mgs.service.dto.ReportTemplateDTO;
import com.mgs.service.dto.TenantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportTemplate} and its DTO {@link ReportTemplateDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportTemplateMapper extends EntityMapper<ReportTemplateDTO, ReportTemplate> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "tenantId")
    ReportTemplateDTO toDto(ReportTemplate s);

    @Named("tenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TenantDTO toDtoTenantId(Tenant tenant);
}
