package com.crm.repository;

import com.crm.domain.MasterStaticType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the MasterStaticType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MasterStaticTypeRepository extends JpaRepository<MasterStaticType, Long>, JpaSpecificationExecutor<MasterStaticType> {}
