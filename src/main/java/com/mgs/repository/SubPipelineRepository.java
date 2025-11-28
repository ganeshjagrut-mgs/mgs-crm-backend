package com.mgs.repository;

import com.mgs.domain.SubPipeline;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SubPipeline entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubPipelineRepository extends JpaRepository<SubPipeline, Long>, JpaSpecificationExecutor<SubPipeline> {}
