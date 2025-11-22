package com.crm.repository;

import com.crm.domain.PipelineTag;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PipelineTag entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PipelineTagRepository extends JpaRepository<PipelineTag, Long>, JpaSpecificationExecutor<PipelineTag> {}
