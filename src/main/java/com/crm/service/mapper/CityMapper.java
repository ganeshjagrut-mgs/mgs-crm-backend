package com.crm.service.mapper;

import com.crm.domain.City;
import com.crm.domain.State;
import com.crm.service.dto.CityDTO;
import com.crm.service.dto.StateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link City} and its DTO {@link CityDTO}.
 */
@Mapper(componentModel = "spring")
public interface CityMapper extends EntityMapper<CityDTO, City> {
    @Mapping(target = "state", source = "state", qualifiedByName = "stateName")
    CityDTO toDto(City s);

    @Named("stateName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    StateDTO toDtoStateName(State state);
}
