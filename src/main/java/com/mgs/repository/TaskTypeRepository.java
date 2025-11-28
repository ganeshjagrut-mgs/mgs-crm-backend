package com.mgs.repository;

import com.mgs.domain.TaskType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TaskType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TaskTypeRepository extends JpaRepository<TaskType, Long>, JpaSpecificationExecutor<TaskType> {}
