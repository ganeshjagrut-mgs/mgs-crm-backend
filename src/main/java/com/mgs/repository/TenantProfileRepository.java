package com.mgs.repository;

import com.mgs.domain.TenantProfile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TenantProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TenantProfileRepository extends JpaRepository<TenantProfile, Long>, JpaSpecificationExecutor<TenantProfile> {}
