package com.crm.repository;

import com.crm.domain.TaskAudit;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TaskAudit entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskAuditRepository extends JpaRepository<TaskAudit, Long>, JpaSpecificationExecutor<TaskAudit> {}
