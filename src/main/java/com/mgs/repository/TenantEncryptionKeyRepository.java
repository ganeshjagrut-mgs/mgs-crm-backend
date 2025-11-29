package com.mgs.repository;

import com.mgs.domain.TenantEncryptionKey;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Spring Data JPA repository for the TenantEncryptionKey entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TenantEncryptionKeyRepository
    extends JpaRepository<TenantEncryptionKey, Long>, JpaSpecificationExecutor<TenantEncryptionKey> {

    /**
     * Find active encryption key by tenant ID.
     *
     * @param tenantId the tenant ID
     * @return Optional containing the active encryption key
     */
    Optional<TenantEncryptionKey> findByTenantIdAndIsActiveTrue(Long tenantId);
}
