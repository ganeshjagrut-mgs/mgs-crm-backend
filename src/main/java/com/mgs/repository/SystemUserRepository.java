package com.mgs.repository;

import com.mgs.domain.SystemUser;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SystemUser entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SystemUserRepository extends JpaRepository<SystemUser, Long>, JpaSpecificationExecutor<SystemUser> {}
