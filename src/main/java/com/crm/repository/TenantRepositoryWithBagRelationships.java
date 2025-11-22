package com.crm.repository;

import com.crm.domain.Tenant;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface TenantRepositoryWithBagRelationships {
    Optional<Tenant> fetchBagRelationships(Optional<Tenant> tenant);

    List<Tenant> fetchBagRelationships(List<Tenant> tenants);

    Page<Tenant> fetchBagRelationships(Page<Tenant> tenants);
}
