package com.mgs.repository;

import com.mgs.domain.TenantSubscription;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TenantSubscription entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TenantSubscriptionRepository
    extends JpaRepository<TenantSubscription, Long>, JpaSpecificationExecutor<TenantSubscription> {}
