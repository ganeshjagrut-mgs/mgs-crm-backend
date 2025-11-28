package com.mgs.repository;

import com.mgs.domain.QuotationItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the QuotationItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuotationItemRepository extends JpaRepository<QuotationItem, Long>, JpaSpecificationExecutor<QuotationItem> {}
