package com.mgs.service.mapper;

import com.mgs.domain.ReportRun;
import com.mgs.domain.ReportTemplate;
import com.mgs.domain.Tenant;
import com.mgs.domain.User;
import com.mgs.service.dto.ReportRunDTO;
import com.mgs.service.dto.ReportTemplateDTO;
import com.mgs.service.dto.TenantDTO;
import com.mgs.service.dto.UserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ReportRun} and its DTO {@link ReportRunDTO}.
 */
@Mapper(componentModel = "spring")
public interface ReportRunMapper extends EntityMapper<ReportRunDTO, ReportRun> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "tenantId")
    @Mapping(target = "template", source = "template", qualifiedByName = "reportTemplateId")
    @Mapping(target = "generatedByUser", source = "generatedByUser", qualifiedByName = "userId")
    ReportRunDTO toDto(ReportRun s);

    @Named("tenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TenantDTO toDtoTenantId(Tenant tenant);

    @Named("reportTemplateId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ReportTemplateDTO toDtoReportTemplateId(ReportTemplate reportTemplate);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);
}
