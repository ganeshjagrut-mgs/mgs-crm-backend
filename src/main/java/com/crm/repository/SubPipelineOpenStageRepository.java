package com.crm.repository;

import com.crm.domain.SubPipelineOpenStage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SubPipelineOpenStage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubPipelineOpenStageRepository
    extends JpaRepository<SubPipelineOpenStage, Long>, JpaSpecificationExecutor<SubPipelineOpenStage> {}
