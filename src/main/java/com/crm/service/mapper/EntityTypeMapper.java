package com.crm.service.mapper;

import com.crm.domain.EntityType;
import com.crm.service.dto.EntityTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EntityType} and its DTO {@link EntityTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface EntityTypeMapper extends EntityMapper<EntityTypeDTO, EntityType> {}
