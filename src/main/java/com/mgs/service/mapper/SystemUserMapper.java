package com.mgs.service.mapper;

import com.mgs.domain.SystemUser;
import com.mgs.service.dto.SystemUserDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SystemUser} and its DTO {@link SystemUserDTO}.
 */
@Mapper(componentModel = "spring")
public interface SystemUserMapper extends EntityMapper<SystemUserDTO, SystemUser> {}
