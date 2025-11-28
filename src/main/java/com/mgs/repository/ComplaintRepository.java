package com.mgs.repository;

import com.mgs.domain.Complaint;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Complaint entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long>, JpaSpecificationExecutor<Complaint> {
    @Query("select complaint from Complaint complaint where complaint.assignedUser.email = ?#{authentication.name}")
    List<Complaint> findByAssignedUserIsCurrentUser();
}
