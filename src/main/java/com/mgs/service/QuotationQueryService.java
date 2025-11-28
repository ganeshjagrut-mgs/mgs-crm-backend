package com.mgs.service;

import com.mgs.domain.*; // for static metamodels
import com.mgs.domain.Quotation;
import com.mgs.repository.QuotationRepository;
import com.mgs.service.criteria.QuotationCriteria;
import com.mgs.service.dto.QuotationDTO;
import com.mgs.service.mapper.QuotationMapper;
import jakarta.persistence.criteria.JoinType;
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
 * It returns a {@link Page} of {@link QuotationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuotationQueryService extends QueryService<Quotation> {

    private static final Logger LOG = LoggerFactory.getLogger(QuotationQueryService.class);

    private final QuotationRepository quotationRepository;

    private final QuotationMapper quotationMapper;

    public QuotationQueryService(QuotationRepository quotationRepository, QuotationMapper quotationMapper) {
        this.quotationRepository = quotationRepository;
        this.quotationMapper = quotationMapper;
    }

    /**
     * Return a {@link Page} of {@link QuotationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QuotationDTO> findByCriteria(QuotationCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
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
        LOG.debug("count by criteria : {}", criteria);
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
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Quotation_.id),
                buildStringSpecification(criteria.getQuoteNumber(), Quotation_.quoteNumber),
                buildRangeSpecification(criteria.getEstimateDate(), Quotation_.estimateDate),
                buildRangeSpecification(criteria.getValidUntil(), Quotation_.validUntil),
                buildSpecification(criteria.getStatus(), Quotation_.status),
                buildRangeSpecification(criteria.getRevisionNumber(), Quotation_.revisionNumber),
                buildStringSpecification(criteria.getCurrency(), Quotation_.currency),
                buildRangeSpecification(criteria.getSubtotal(), Quotation_.subtotal),
                buildRangeSpecification(criteria.getItemDiscountTotal(), Quotation_.itemDiscountTotal),
                buildSpecification(criteria.getGlobalDiscountType(), Quotation_.globalDiscountType),
                buildRangeSpecification(criteria.getGlobalDiscountValue(), Quotation_.globalDiscountValue),
                buildRangeSpecification(criteria.getGlobalDiscountAmount(), Quotation_.globalDiscountAmount),
                buildRangeSpecification(criteria.getTaxableAmount(), Quotation_.taxableAmount),
                buildRangeSpecification(criteria.getTotalTax(), Quotation_.totalTax),
                buildRangeSpecification(criteria.getShippingAmount(), Quotation_.shippingAmount),
                buildRangeSpecification(criteria.getOtherChargesAmount(), Quotation_.otherChargesAmount),
                buildRangeSpecification(criteria.getRoundOffAmount(), Quotation_.roundOffAmount),
                buildRangeSpecification(criteria.getTotalAmount(), Quotation_.totalAmount),
                buildStringSpecification(criteria.getTitle(), Quotation_.title),
                buildStringSpecification(criteria.getHeaderNotes(), Quotation_.headerNotes),
                buildStringSpecification(criteria.getFooterNotes(), Quotation_.footerNotes),
                buildStringSpecification(criteria.getTermsAndConditions(), Quotation_.termsAndConditions),
                buildStringSpecification(criteria.getReferenceNumber(), Quotation_.referenceNumber),
                buildRangeSpecification(criteria.getLastSentAt(), Quotation_.lastSentAt),
                buildStringSpecification(criteria.getPdfTemplateCode(), Quotation_.pdfTemplateCode),
                buildSpecification(criteria.getTenantId(), root -> root.join(Quotation_.tenant, JoinType.LEFT).get(Tenant_.id)),
                buildSpecification(criteria.getCustomerId(), root -> root.join(Quotation_.customer, JoinType.LEFT).get(Customer_.id)),
                buildSpecification(criteria.getContactId(), root -> root.join(Quotation_.contact, JoinType.LEFT).get(Contact_.id)),
                buildSpecification(criteria.getLeadId(), root -> root.join(Quotation_.lead, JoinType.LEFT).get(Lead_.id)),
                buildSpecification(criteria.getCreatedByUserId(), root -> root.join(Quotation_.createdByUser, JoinType.LEFT).get(User_.id))
            );
        }
        return specification;
    }
}
