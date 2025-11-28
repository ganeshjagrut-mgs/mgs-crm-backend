package com.mgs.repository;

import com.mgs.domain.TaskComment;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TaskComment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskCommentRepository extends JpaRepository<TaskComment, Long>, JpaSpecificationExecutor<TaskComment> {
    @Query("select taskComment from TaskComment taskComment where taskComment.createdByUser.email = ?#{authentication.name}")
    List<TaskComment> findByCreatedByUserIsCurrentUser();
}
