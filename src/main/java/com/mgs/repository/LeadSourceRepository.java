package com.mgs.repository;

import com.mgs.domain.LeadSource;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the LeadSource entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeadSourceRepository extends JpaRepository<LeadSource, Long>, JpaSpecificationExecutor<LeadSource> {}
