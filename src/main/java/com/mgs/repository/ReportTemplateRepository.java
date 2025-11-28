package com.mgs.repository;

import com.mgs.domain.ReportTemplate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReportTemplate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportTemplateRepository extends JpaRepository<ReportTemplate, Long>, JpaSpecificationExecutor<ReportTemplate> {}
