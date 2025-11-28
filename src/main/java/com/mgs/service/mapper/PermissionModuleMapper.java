package com.mgs.service.mapper;

import com.mgs.domain.PermissionModule;
import com.mgs.service.dto.PermissionModuleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PermissionModule} and its DTO {@link PermissionModuleDTO}.
 */
@Mapper(componentModel = "spring")
public interface PermissionModuleMapper extends EntityMapper<PermissionModuleDTO, PermissionModule> {}
