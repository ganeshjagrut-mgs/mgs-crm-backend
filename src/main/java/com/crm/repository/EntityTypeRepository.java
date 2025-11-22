package com.crm.repository;

import com.crm.domain.EntityType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EntityType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EntityTypeRepository extends JpaRepository<EntityType, Long>, JpaSpecificationExecutor<EntityType> {}
