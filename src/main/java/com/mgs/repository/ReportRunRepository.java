package com.mgs.repository;

import com.mgs.domain.ReportRun;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ReportRun entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ReportRunRepository extends JpaRepository<ReportRun, Long>, JpaSpecificationExecutor<ReportRun> {
    @Query("select reportRun from ReportRun reportRun where reportRun.generatedByUser.email = ?#{authentication.name}")
    List<ReportRun> findByGeneratedByUserIsCurrentUser();
}
