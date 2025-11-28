package com.mgs.repository;

import com.mgs.domain.AuditLog;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AuditLog entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long>, JpaSpecificationExecutor<AuditLog> {
    @Query("select auditLog from AuditLog auditLog where auditLog.performedBy.email = ?#{authentication.name}")
    List<AuditLog> findByPerformedByIsCurrentUser();
}
