package com.mgs.repository;

import com.mgs.domain.TenantEncryptionKey;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TenantEncryptionKey entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TenantEncryptionKeyRepository
    extends JpaRepository<TenantEncryptionKey, Long>, JpaSpecificationExecutor<TenantEncryptionKey> {}
