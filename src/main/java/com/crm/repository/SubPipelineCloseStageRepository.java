package com.crm.repository;

import com.crm.domain.SubPipelineCloseStage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SubPipelineCloseStage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SubPipelineCloseStageRepository
    extends JpaRepository<SubPipelineCloseStage, Long>, JpaSpecificationExecutor<SubPipelineCloseStage> {}
