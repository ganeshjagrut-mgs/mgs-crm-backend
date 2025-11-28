package com.mgs.repository;

import com.mgs.domain.Task;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Task entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskRepository extends JpaRepository<Task, Long>, JpaSpecificationExecutor<Task> {
    @Query("select task from Task task where task.assignedUser.email = ?#{authentication.name}")
    List<Task> findByAssignedUserIsCurrentUser();

    @Query("select task from Task task where task.createdByUser.email = ?#{authentication.name}")
    List<Task> findByCreatedByUserIsCurrentUser();
}
