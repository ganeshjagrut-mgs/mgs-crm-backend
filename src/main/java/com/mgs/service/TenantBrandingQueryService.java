package com.mgs.service;

import com.mgs.domain.*; // for static metamodels
import com.mgs.domain.TenantBranding;
import com.mgs.repository.TenantBrandingRepository;
import com.mgs.service.criteria.TenantBrandingCriteria;
import com.mgs.service.dto.TenantBrandingDTO;
import com.mgs.service.mapper.TenantBrandingMapper;
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
 * Service for executing complex queries for {@link TenantBranding} entities in the database.
 * The main input is a {@link TenantBrandingCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TenantBrandingDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TenantBrandingQueryService extends QueryService<TenantBranding> {

    private static final Logger LOG = LoggerFactory.getLogger(TenantBrandingQueryService.class);

    private final TenantBrandingRepository tenantBrandingRepository;

    private final TenantBrandingMapper tenantBrandingMapper;

    public TenantBrandingQueryService(TenantBrandingRepository tenantBrandingRepository, TenantBrandingMapper tenantBrandingMapper) {
        this.tenantBrandingRepository = tenantBrandingRepository;
        this.tenantBrandingMapper = tenantBrandingMapper;
    }

    /**
     * Return a {@link Page} of {@link TenantBrandingDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TenantBrandingDTO> findByCriteria(TenantBrandingCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TenantBranding> specification = createSpecification(criteria);
        return tenantBrandingRepository.findAll(specification, page).map(tenantBrandingMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TenantBrandingCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TenantBranding> specification = createSpecification(criteria);
        return tenantBrandingRepository.count(specification);
    }

    /**
     * Function to convert {@link TenantBrandingCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TenantBranding> createSpecification(TenantBrandingCriteria criteria) {
        Specification<TenantBranding> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), TenantBranding_.id),
                buildStringSpecification(criteria.getLogoPath(), TenantBranding_.logoPath),
                buildStringSpecification(criteria.getLogoDarkPath(), TenantBranding_.logoDarkPath),
                buildStringSpecification(criteria.getFaviconPath(), TenantBranding_.faviconPath),
                buildStringSpecification(criteria.getPrimaryColor(), TenantBranding_.primaryColor),
                buildStringSpecification(criteria.getSecondaryColor(), TenantBranding_.secondaryColor),
                buildStringSpecification(criteria.getAccentColor(), TenantBranding_.accentColor),
                buildStringSpecification(criteria.getPdfHeaderLogoPath(), TenantBranding_.pdfHeaderLogoPath),
                buildStringSpecification(criteria.getPdfFooterText(), TenantBranding_.pdfFooterText),
                buildStringSpecification(criteria.getPdfPrimaryColor(), TenantBranding_.pdfPrimaryColor),
                buildSpecification(criteria.getTenantId(), root -> root.join(TenantBranding_.tenant, JoinType.LEFT).get(Tenant_.id))
            );
        }
        return specification;
    }
}
