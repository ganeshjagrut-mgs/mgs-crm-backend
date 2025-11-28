package com.mgs.repository;

import com.mgs.domain.Department;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Department entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long>, JpaSpecificationExecutor<Department> {
    @Query("select department from Department department where department.headUser.email = ?#{authentication.name}")
    List<Department> findByHeadUserIsCurrentUser();
}
