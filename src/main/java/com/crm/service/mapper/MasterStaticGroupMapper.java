package com.crm.service.mapper;

import com.crm.domain.EntityType;
import com.crm.domain.MasterStaticGroup;
import com.crm.service.dto.EntityTypeDTO;
import com.crm.service.dto.MasterStaticGroupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MasterStaticGroup} and its DTO {@link MasterStaticGroupDTO}.
 */
@Mapper(componentModel = "spring")
public interface MasterStaticGroupMapper extends EntityMapper<MasterStaticGroupDTO, MasterStaticGroup> {
    @Mapping(target = "entityType", source = "entityType", qualifiedByName = "entityTypeId")
    MasterStaticGroupDTO toDto(MasterStaticGroup s);

    @Named("entityTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EntityTypeDTO toDtoEntityTypeId(EntityType entityType);
}
