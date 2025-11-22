package com.crm.service.mapper;

import com.crm.domain.MasterCategory;
import com.crm.service.dto.MasterCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MasterCategory} and its DTO {@link MasterCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface MasterCategoryMapper extends EntityMapper<MasterCategoryDTO, MasterCategory> {}
