package com.mgs.service.mapper;

import com.mgs.domain.Plan;
import com.mgs.domain.Tenant;
import com.mgs.domain.TenantSubscription;
import com.mgs.service.dto.PlanDTO;
import com.mgs.service.dto.TenantDTO;
import com.mgs.service.dto.TenantSubscriptionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TenantSubscription} and its DTO {@link TenantSubscriptionDTO}.
 */
@Mapper(componentModel = "spring")
public interface TenantSubscriptionMapper extends EntityMapper<TenantSubscriptionDTO, TenantSubscription> {
    @Mapping(target = "tenant", source = "tenant", qualifiedByName = "tenantId")
    @Mapping(target = "plan", source = "plan", qualifiedByName = "planId")
    TenantSubscriptionDTO toDto(TenantSubscription s);

    @Named("tenantId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TenantDTO toDtoTenantId(Tenant tenant);

    @Named("planId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PlanDTO toDtoPlanId(Plan plan);
}
