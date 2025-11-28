package com.mgs.service.mapper;

import com.mgs.domain.ComplaintCategory;
import com.mgs.domain.Tenant;
import com.mgs.service.dto.ComplaintCategoryDTO;
import com.mgs.service.dto.TenantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ComplaintCategory} and its DTO {@link ComplaintCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface ComplaintCategoryMapper extends EntityMapper<ComplaintCategoryDTO, ComplaintCategory> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "tenantId")
    ComplaintCategoryDTO toDto(ComplaintCategory s);

    @Named("tenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TenantDTO toDtoTenantId(Tenant tenant);
}
