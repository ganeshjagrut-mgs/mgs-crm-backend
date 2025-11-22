package com.crm.repository;

import com.crm.domain.MasterCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MasterCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MasterCategoryRepository extends JpaRepository<MasterCategory, Long>, JpaSpecificationExecutor<MasterCategory> {}
