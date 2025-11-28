package com.mgs.service;

import com.mgs.domain.*; // for static metamodels
import com.mgs.domain.TenantSubscription;
import com.mgs.repository.TenantSubscriptionRepository;
import com.mgs.service.criteria.TenantSubscriptionCriteria;
import com.mgs.service.dto.TenantSubscriptionDTO;
import com.mgs.service.mapper.TenantSubscriptionMapper;
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
 * Service for executing complex queries for {@link TenantSubscription} entities in the database.
 * The main input is a {@link TenantSubscriptionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TenantSubscriptionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TenantSubscriptionQueryService extends QueryService<TenantSubscription> {

    private static final Logger LOG = LoggerFactory.getLogger(TenantSubscriptionQueryService.class);

    private final TenantSubscriptionRepository tenantSubscriptionRepository;

    private final TenantSubscriptionMapper tenantSubscriptionMapper;

    public TenantSubscriptionQueryService(
        TenantSubscriptionRepository tenantSubscriptionRepository,
        TenantSubscriptionMapper tenantSubscriptionMapper
    ) {
        this.tenantSubscriptionRepository = tenantSubscriptionRepository;
        this.tenantSubscriptionMapper = tenantSubscriptionMapper;
    }

    /**
     * Return a {@link Page} of {@link TenantSubscriptionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TenantSubscriptionDTO> findByCriteria(TenantSubscriptionCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TenantSubscription> specification = createSpecification(criteria);
        return tenantSubscriptionRepository.findAll(specification, page).map(tenantSubscriptionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TenantSubscriptionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TenantSubscription> specification = createSpecification(criteria);
        return tenantSubscriptionRepository.count(specification);
    }

    /**
     * Function to convert {@link TenantSubscriptionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TenantSubscription> createSpecification(TenantSubscriptionCriteria criteria) {
        Specification<TenantSubscription> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), TenantSubscription_.id),
                buildSpecification(criteria.getStatus(), TenantSubscription_.status),
                buildRangeSpecification(criteria.getStartDate(), TenantSubscription_.startDate),
                buildRangeSpecification(criteria.getEndDate(), TenantSubscription_.endDate),
                buildRangeSpecification(criteria.getTrialEndDate(), TenantSubscription_.trialEndDate),
                buildRangeSpecification(criteria.getLastRenewedAt(), TenantSubscription_.lastRenewedAt),
                buildRangeSpecification(criteria.getNextBillingAt(), TenantSubscription_.nextBillingAt),
                buildSpecification(criteria.getTenantId(), root -> root.join(TenantSubscription_.tenant, JoinType.LEFT).get(Tenant_.id)),
                buildSpecification(criteria.getPlanId(), root -> root.join(TenantSubscription_.plan, JoinType.LEFT).get(Plan_.id))
            );
        }
        return specification;
    }
}
