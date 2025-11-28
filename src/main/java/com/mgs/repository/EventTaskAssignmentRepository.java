package com.mgs.repository;

import com.mgs.domain.EventTaskAssignment;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EventTaskAssignment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EventTaskAssignmentRepository
    extends JpaRepository<EventTaskAssignment, Long>, JpaSpecificationExecutor<EventTaskAssignment> {
    @Query(
        "select eventTaskAssignment from EventTaskAssignment eventTaskAssignment where eventTaskAssignment.assignedTo.email = ?#{authentication.name}"
    )
    List<EventTaskAssignment> findByAssignedToIsCurrentUser();
}
