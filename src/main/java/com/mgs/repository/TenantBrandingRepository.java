package com.mgs.repository;

import com.mgs.domain.TenantBranding;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TenantBranding entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TenantBrandingRepository extends JpaRepository<TenantBranding, Long>, JpaSpecificationExecutor<TenantBranding> {}
