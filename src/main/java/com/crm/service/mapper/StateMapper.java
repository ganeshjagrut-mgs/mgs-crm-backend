package com.crm.service.mapper;

import com.crm.domain.Country;
import com.crm.domain.State;
import com.crm.service.dto.CountryDTO;
import com.crm.service.dto.StateDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link State} and its DTO {@link StateDTO}.
 */
@Mapper(componentModel = "spring")
public interface StateMapper extends EntityMapper<StateDTO, State> {
    @Mapping(target = "country", source = "country", qualifiedByName = "countryName")
    StateDTO toDto(State s);

    @Named("countryName")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    CountryDTO toDtoCountryName(Country country);
}
