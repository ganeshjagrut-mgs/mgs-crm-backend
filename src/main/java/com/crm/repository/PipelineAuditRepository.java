package com.crm.repository;

import com.crm.domain.PipelineAudit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PipelineAudit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PipelineAuditRepository extends JpaRepository<PipelineAudit, Long>, JpaSpecificationExecutor<PipelineAudit> {}
