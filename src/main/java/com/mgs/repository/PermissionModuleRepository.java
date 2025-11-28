package com.mgs.repository;

import com.mgs.domain.PermissionModule;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the PermissionModule entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PermissionModuleRepository extends JpaRepository<PermissionModule, Long>, JpaSpecificationExecutor<PermissionModule> {}
