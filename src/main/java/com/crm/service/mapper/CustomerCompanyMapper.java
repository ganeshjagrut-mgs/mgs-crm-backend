package com.crm.service.mapper;

import com.crm.domain.CustomerCompany;
import com.crm.service.dto.CustomerCompanyDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CustomerCompany} and its DTO {@link CustomerCompanyDTO}.
 */
@Mapper(componentModel = "spring")
public interface CustomerCompanyMapper extends EntityMapper<CustomerCompanyDTO, CustomerCompany> {}
