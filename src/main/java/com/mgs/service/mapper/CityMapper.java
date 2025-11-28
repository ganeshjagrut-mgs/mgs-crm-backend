package com.mgs.service.mapper;

import com.mgs.domain.City;
import com.mgs.domain.State;
import com.mgs.service.dto.CityDTO;
import com.mgs.service.dto.StateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link City} and its DTO {@link CityDTO}.
 */
@Mapper(componentModel = "spring")
public interface CityMapper extends EntityMapper<CityDTO, City> {
    @Mapping(target = "state", source = "state", qualifiedByName = "stateId")
    CityDTO toDto(City s);

    @Named("stateId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StateDTO toDtoStateId(State state);
}
