package com.mgs.service;

import com.mgs.domain.*; // for static metamodels
import com.mgs.domain.TenantProfile;
import com.mgs.repository.TenantProfileRepository;
import com.mgs.service.criteria.TenantProfileCriteria;
import com.mgs.service.dto.TenantProfileDTO;
import com.mgs.service.mapper.TenantProfileMapper;
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
 * Service for executing complex queries for {@link TenantProfile} entities in the database.
 * The main input is a {@link TenantProfileCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TenantProfileDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TenantProfileQueryService extends QueryService<TenantProfile> {

    private static final Logger LOG = LoggerFactory.getLogger(TenantProfileQueryService.class);

    private final TenantProfileRepository tenantProfileRepository;

    private final TenantProfileMapper tenantProfileMapper;

    public TenantProfileQueryService(TenantProfileRepository tenantProfileRepository, TenantProfileMapper tenantProfileMapper) {
        this.tenantProfileRepository = tenantProfileRepository;
        this.tenantProfileMapper = tenantProfileMapper;
    }

    /**
     * Return a {@link Page} of {@link TenantProfileDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TenantProfileDTO> findByCriteria(TenantProfileCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TenantProfile> specification = createSpecification(criteria);
        return tenantProfileRepository.findAll(specification, page).map(tenantProfileMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TenantProfileCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TenantProfile> specification = createSpecification(criteria);
        return tenantProfileRepository.count(specification);
    }

    /**
     * Function to convert {@link TenantProfileCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TenantProfile> createSpecification(TenantProfileCriteria criteria) {
        Specification<TenantProfile> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), TenantProfile_.id),
                buildStringSpecification(criteria.getSubdomain(), TenantProfile_.subdomain),
                buildStringSpecification(criteria.getCustomDomain(), TenantProfile_.customDomain),
                buildSpecification(criteria.getDomainVerified(), TenantProfile_.domainVerified),
                buildStringSpecification(criteria.getDefaultLocale(), TenantProfile_.defaultLocale),
                buildStringSpecification(criteria.getTimezone(), TenantProfile_.timezone),
                buildSpecification(criteria.getTenantId(), root -> root.join(TenantProfile_.tenant, JoinType.LEFT).get(Tenant_.id)),
                buildSpecification(criteria.getAddressId(), root -> root.join(TenantProfile_.address, JoinType.LEFT).get(Address_.id))
            );
        }
        return specification;
    }
}
