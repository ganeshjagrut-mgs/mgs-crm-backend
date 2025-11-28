package com.mgs.repository;

import com.mgs.domain.UserRole;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserRole entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long>, JpaSpecificationExecutor<UserRole> {
    @Query("select userRole from UserRole userRole where userRole.user.email = ?#{authentication.name}")
    List<UserRole> findByUserIsCurrentUser();
}
