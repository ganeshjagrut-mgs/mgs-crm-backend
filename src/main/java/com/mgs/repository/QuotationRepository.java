package com.mgs.repository;

import com.mgs.domain.Quotation;
import java.util.List;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Quotation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuotationRepository extends JpaRepository<Quotation, Long>, JpaSpecificationExecutor<Quotation> {
    @Query("select quotation from Quotation quotation where quotation.createdByUser.email = ?#{authentication.name}")
    List<Quotation> findByCreatedByUserIsCurrentUser();
}
