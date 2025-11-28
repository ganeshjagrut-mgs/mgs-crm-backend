package com.mgs.service.mapper;

import com.mgs.domain.Tenant;
import com.mgs.service.dto.TenantDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tenant} and its DTO {@link TenantDTO}.
 */
@Mapper(componentModel = "spring")
public interface TenantMapper extends EntityMapper<TenantDTO, Tenant> {}
