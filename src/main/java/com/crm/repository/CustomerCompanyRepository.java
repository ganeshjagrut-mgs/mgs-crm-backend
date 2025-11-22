package com.crm.repository;

import com.crm.domain.CustomerCompany;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CustomerCompany entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CustomerCompanyRepository extends JpaRepository<CustomerCompany, Long>, JpaSpecificationExecutor<CustomerCompany> {}
