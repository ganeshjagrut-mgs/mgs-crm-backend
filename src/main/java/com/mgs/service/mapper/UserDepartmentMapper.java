package com.mgs.service.mapper;

import com.mgs.domain.Department;
import com.mgs.domain.Tenant;
import com.mgs.domain.User;
import com.mgs.domain.UserDepartment;
import com.mgs.service.dto.DepartmentDTO;
import com.mgs.service.dto.TenantDTO;
import com.mgs.service.dto.UserDTO;
import com.mgs.service.dto.UserDepartmentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link UserDepartment} and its DTO {@link UserDepartmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface UserDepartmentMapper extends EntityMapper<UserDepartmentDTO, UserDepartment> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "tenantId")
    @Mapping(target = "user", source = "user", qualifiedByName = "userId")
    @Mapping(target = "department", source = "department", qualifiedByName = "departmentId")
    UserDepartmentDTO toDto(UserDepartment s);

    @Named("tenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TenantDTO toDtoTenantId(Tenant tenant);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("departmentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DepartmentDTO toDtoDepartmentId(Department department);
}
