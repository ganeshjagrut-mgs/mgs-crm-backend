package com.crm.service.mapper;

import com.crm.domain.MasterStaticType;
import com.crm.service.dto.MasterStaticTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MasterStaticType} and its DTO {@link MasterStaticTypeDTO}.
 */
@Mapper(componentModel = "spring")
public interface MasterStaticTypeMapper extends EntityMapper<MasterStaticTypeDTO, MasterStaticType> {}
