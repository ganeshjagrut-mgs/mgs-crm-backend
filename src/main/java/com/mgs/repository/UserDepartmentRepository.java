package com.mgs.repository;

import com.mgs.domain.UserDepartment;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserDepartment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserDepartmentRepository extends JpaRepository<UserDepartment, Long>, JpaSpecificationExecutor<UserDepartment> {
    @Query("select userDepartment from UserDepartment userDepartment where userDepartment.user.email = ?#{authentication.name}")
    List<UserDepartment> findByUserIsCurrentUser();
}
