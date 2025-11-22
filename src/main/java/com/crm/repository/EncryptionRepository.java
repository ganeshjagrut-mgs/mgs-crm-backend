package com.crm.repository;

import com.crm.domain.Encryption;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Encryption entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EncryptionRepository extends JpaRepository<Encryption, Long>, JpaSpecificationExecutor<Encryption> {}
