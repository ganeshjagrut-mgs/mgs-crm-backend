package com.crm.service.mapper;

import com.crm.domain.Complaint;
import com.crm.domain.Customer;
import com.crm.domain.MasterStaticType;
import com.crm.domain.User;
import com.crm.service.dto.ComplaintDTO;
import com.crm.service.dto.CustomerDTO;
import com.crm.service.dto.MasterStaticTypeDTO;
import com.crm.service.dto.UserDTO;
import java.util.Set;
import java.util.stream.Collectors;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Complaint} and its DTO {@link ComplaintDTO}.
 */
@Mapper(componentModel = "spring")
public interface ComplaintMapper extends EntityMapper<ComplaintDTO, Complaint> {
    @Mapping(target = "customerName", source = "customerName", qualifiedByName = "customerId")
    @Mapping(target = "complaintRelatedTo", source = "complaintRelatedTo", qualifiedByName = "masterStaticTypeId")
    @Mapping(target = "typeOfComplaint", source = "typeOfComplaint", qualifiedByName = "masterStaticTypeId")
    @Mapping(target = "complaintRelatedPersons", source = "complaintRelatedPersons", qualifiedByName = "userIdSet")
    ComplaintDTO toDto(Complaint s);

    @Mapping(target = "removeComplaintRelatedPersons", ignore = true)
    Complaint toEntity(ComplaintDTO complaintDTO);

    @Named("customerId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CustomerDTO toDtoCustomerId(Customer customer);

    @Named("masterStaticTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MasterStaticTypeDTO toDtoMasterStaticTypeId(MasterStaticType masterStaticType);

    @Named("userId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UserDTO toDtoUserId(User user);

    @Named("userIdSet")
    default Set<UserDTO> toDtoUserIdSet(Set<User> user) {
        return user.stream().map(this::toDtoUserId).collect(Collectors.toSet());
    }
}
