package com.crm.repository;

import com.crm.domain.MasterStaticGroup;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MasterStaticGroup entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MasterStaticGroupRepository extends JpaRepository<MasterStaticGroup, Long>, JpaSpecificationExecutor<MasterStaticGroup> {}
