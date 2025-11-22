package com.crm.service;

import com.crm.domain.*; // for static metamodels
import com.crm.domain.Quotation;
import com.crm.repository.QuotationRepository;
import com.crm.service.criteria.QuotationCriteria;
import com.crm.service.dto.QuotationDTO;
import com.crm.service.mapper.QuotationMapper;
import jakarta.persistence.criteria.JoinType;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link Quotation} entities in the database.
 * The main input is a {@link QuotationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link QuotationDTO} or a {@link Page} of {@link QuotationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuotationQueryService extends QueryService<Quotation> {

    private final Logger log = LoggerFactory.getLogger(QuotationQueryService.class);

    private final QuotationRepository quotationRepository;

    private final QuotationMapper quotationMapper;

    public QuotationQueryService(QuotationRepository quotationRepository, QuotationMapper quotationMapper) {
        this.quotationRepository = quotationRepository;
        this.quotationMapper = quotationMapper;
    }

    /**
     * Return a {@link List} of {@link QuotationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<QuotationDTO> findByCriteria(QuotationCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Quotation> specification = createSpecification(criteria);
        return quotationMapper.toDto(quotationRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link QuotationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QuotationDTO> findByCriteria(QuotationCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Quotation> specification = createSpecification(criteria);
        return quotationRepository.findAll(specification, page).map(quotationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QuotationCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Quotation> specification = createSpecification(criteria);
        return quotationRepository.count(specification);
    }

    /**
     * Function to convert {@link QuotationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Quotation> createSpecification(QuotationCriteria criteria) {
        Specification<Quotation> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Quotation_.id));
            }
            if (criteria.getQuotationNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getQuotationNumber(), Quotation_.quotationNumber));
            }
            if (criteria.getQuotationDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuotationDate(), Quotation_.quotationDate));
            }
            if (criteria.getReferenceNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getReferenceNumber(), Quotation_.referenceNumber));
            }
            if (criteria.getReferenceDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getReferenceDate(), Quotation_.referenceDate));
            }
            if (criteria.getEstimateDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEstimateDate(), Quotation_.estimateDate));
            }
            if (criteria.getSubject() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSubject(), Quotation_.subject));
            }
            if (criteria.getValidity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getValidity(), Quotation_.validity));
            }
            if (criteria.getDiscountLevelType() != null) {
                specification = specification.and(buildSpecification(criteria.getDiscountLevelType(), Quotation_.discountLevelType));
            }
            if (criteria.getDiscountType() != null) {
                specification = specification.and(buildSpecification(criteria.getDiscountType(), Quotation_.discountType));
            }
            if (criteria.getDiscountTypeValue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDiscountTypeValue(), Quotation_.discountTypeValue));
            }
            if (criteria.getCurrency() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCurrency(), Quotation_.currency));
            }
            if (criteria.getSubTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSubTotal(), Quotation_.subTotal));
            }
            if (criteria.getGrandTotal() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getGrandTotal(), Quotation_.grandTotal));
            }
            if (criteria.getTotalTaxAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalTaxAmount(), Quotation_.totalTaxAmount));
            }
            if (criteria.getAdjustmentAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAdjustmentAmount(), Quotation_.adjustmentAmount));
            }
            if (criteria.getStatusReason() != null) {
                specification = specification.and(buildStringSpecification(criteria.getStatusReason(), Quotation_.statusReason));
            }
            if (criteria.getPdfGenerationStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getPdfGenerationStatus(), Quotation_.pdfGenerationStatus));
            }
            if (criteria.getEmailStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getEmailStatus(), Quotation_.emailStatus));
            }
            if (criteria.getEmailFailureReason() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getEmailFailureReason(), Quotation_.emailFailureReason));
            }
            if (criteria.getCorrelationId() != null) {
                specification = specification.and(buildSpecification(criteria.getCorrelationId(), Quotation_.correlationId));
            }
            if (criteria.getApprovedAt() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getApprovedAt(), Quotation_.approvedAt));
            }
            if (criteria.getPriceDataSource() != null) {
                specification = specification.and(buildSpecification(criteria.getPriceDataSource(), Quotation_.priceDataSource));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getUserId(), root -> root.join(Quotation_.user, JoinType.LEFT).get(User_.id))
                    );
            }
            if (criteria.getCustomerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomerId(),
                            root -> root.join(Quotation_.customer, JoinType.LEFT).get(Customer_.id)
                        )
                    );
            }
            if (criteria.getPaymentTermId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPaymentTermId(),
                            root -> root.join(Quotation_.paymentTerm, JoinType.LEFT).get(MasterStaticType_.id)
                        )
                    );
            }
            if (criteria.getQuotationStatusId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getQuotationStatusId(),
                            root -> root.join(Quotation_.quotationStatus, JoinType.LEFT).get(MasterStaticType_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
