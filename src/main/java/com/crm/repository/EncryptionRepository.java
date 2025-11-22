package com.crm.repository;

import com.crm.domain.Encryption;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Encryption entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EncryptionRepository extends JpaRepository<Encryption, Long>, JpaSpecificationExecutor<Encryption> {
    Optional<Encryption> findByTenantId(Long tenantId);
}
