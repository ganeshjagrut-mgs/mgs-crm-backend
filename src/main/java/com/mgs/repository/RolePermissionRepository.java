package com.mgs.repository;

import com.mgs.domain.RolePermission;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the RolePermission entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long>, JpaSpecificationExecutor<RolePermission> {}
