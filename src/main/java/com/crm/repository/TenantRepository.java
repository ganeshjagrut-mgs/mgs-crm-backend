package com.crm.repository;

import com.crm.domain.Tenant;
import com.crm.domain.Tenant;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Tenant entity.
 *
 * When extending this class, extend TenantRepositoryWithBagRelationships too.
 * For more information refer to
 * https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface TenantRepository
        extends JpaRepository<Tenant, Long>, JpaSpecificationExecutor<Tenant> {
}
