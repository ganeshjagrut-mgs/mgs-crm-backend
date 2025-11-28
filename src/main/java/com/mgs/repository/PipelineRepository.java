package com.mgs.repository;

import com.mgs.domain.Pipeline;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Pipeline entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PipelineRepository extends JpaRepository<Pipeline, Long>, JpaSpecificationExecutor<Pipeline> {}
