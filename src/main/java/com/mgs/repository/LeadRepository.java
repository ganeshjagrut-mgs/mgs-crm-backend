package com.mgs.repository;

import com.mgs.domain.Lead;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Lead entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LeadRepository extends JpaRepository<Lead, Long>, JpaSpecificationExecutor<Lead> {
    @Query("select lead from Lead lead where lead.ownerUser.email = ?#{authentication.name}")
    List<Lead> findByOwnerUserIsCurrentUser();
}
