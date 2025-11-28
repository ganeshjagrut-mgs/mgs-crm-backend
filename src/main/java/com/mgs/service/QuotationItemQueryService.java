package com.mgs.service;

import com.mgs.domain.*; // for static metamodels
import com.mgs.domain.QuotationItem;
import com.mgs.repository.QuotationItemRepository;
import com.mgs.service.criteria.QuotationItemCriteria;
import com.mgs.service.dto.QuotationItemDTO;
import com.mgs.service.mapper.QuotationItemMapper;
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
 * Service for executing complex queries for {@link QuotationItem} entities in the database.
 * The main input is a {@link QuotationItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link QuotationItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuotationItemQueryService extends QueryService<QuotationItem> {

    private static final Logger LOG = LoggerFactory.getLogger(QuotationItemQueryService.class);

    private final QuotationItemRepository quotationItemRepository;

    private final QuotationItemMapper quotationItemMapper;

    public QuotationItemQueryService(QuotationItemRepository quotationItemRepository, QuotationItemMapper quotationItemMapper) {
        this.quotationItemRepository = quotationItemRepository;
        this.quotationItemMapper = quotationItemMapper;
    }

    /**
     * Return a {@link Page} of {@link QuotationItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QuotationItemDTO> findByCriteria(QuotationItemCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<QuotationItem> specification = createSpecification(criteria);
        return quotationItemRepository.findAll(specification, page).map(quotationItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QuotationItemCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<QuotationItem> specification = createSpecification(criteria);
        return quotationItemRepository.count(specification);
    }

    /**
     * Function to convert {@link QuotationItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<QuotationItem> createSpecification(QuotationItemCriteria criteria) {
        Specification<QuotationItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), QuotationItem_.id),
                buildStringSpecification(criteria.getProductName(), QuotationItem_.productName),
                buildStringSpecification(criteria.getProductSku(), QuotationItem_.productSku),
                buildStringSpecification(criteria.getProductDescription(), QuotationItem_.productDescription),
                buildStringSpecification(criteria.getUnitLabel(), QuotationItem_.unitLabel),
                buildRangeSpecification(criteria.getQuantity(), QuotationItem_.quantity),
                buildRangeSpecification(criteria.getUnitPrice(), QuotationItem_.unitPrice),
                buildSpecification(criteria.getDiscountType(), QuotationItem_.discountType),
                buildRangeSpecification(criteria.getDiscountValue(), QuotationItem_.discountValue),
                buildRangeSpecification(criteria.getDiscountAmount(), QuotationItem_.discountAmount),
                buildRangeSpecification(criteria.getTaxableAmount(), QuotationItem_.taxableAmount),
                buildRangeSpecification(criteria.getTaxRate(), QuotationItem_.taxRate),
                buildRangeSpecification(criteria.getTaxAmount(), QuotationItem_.taxAmount),
                buildRangeSpecification(criteria.getLineTotal(), QuotationItem_.lineTotal),
                buildRangeSpecification(criteria.getSortOrder(), QuotationItem_.sortOrder),
                buildSpecification(criteria.getTenantId(), root -> root.join(QuotationItem_.tenant, JoinType.LEFT).get(Tenant_.id)),
                buildSpecification(criteria.getQuotationId(), root -> root.join(QuotationItem_.quotation, JoinType.LEFT).get(Quotation_.id)
                ),
                buildSpecification(criteria.getProductId(), root -> root.join(QuotationItem_.product, JoinType.LEFT).get(Product_.id))
            );
        }
        return specification;
    }
}
