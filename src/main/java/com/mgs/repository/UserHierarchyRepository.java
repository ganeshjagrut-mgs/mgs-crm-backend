package com.mgs.repository;

import com.mgs.domain.UserHierarchy;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserHierarchy entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserHierarchyRepository extends JpaRepository<UserHierarchy, Long>, JpaSpecificationExecutor<UserHierarchy> {
    @Query("select userHierarchy from UserHierarchy userHierarchy where userHierarchy.parentUser.email = ?#{authentication.name}")
    List<UserHierarchy> findByParentUserIsCurrentUser();

    @Query("select userHierarchy from UserHierarchy userHierarchy where userHierarchy.childUser.email = ?#{authentication.name}")
    List<UserHierarchy> findByChildUserIsCurrentUser();
}
