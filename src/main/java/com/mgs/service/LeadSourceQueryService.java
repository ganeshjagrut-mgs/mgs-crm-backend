package com.mgs.service;

import com.mgs.domain.*; // for static metamodels
import com.mgs.domain.LeadSource;
import com.mgs.repository.LeadSourceRepository;
import com.mgs.service.criteria.LeadSourceCriteria;
import com.mgs.service.dto.LeadSourceDTO;
import com.mgs.service.mapper.LeadSourceMapper;
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
 * Service for executing complex queries for {@link LeadSource} entities in the database.
 * The main input is a {@link LeadSourceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link LeadSourceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeadSourceQueryService extends QueryService<LeadSource> {

    private static final Logger LOG = LoggerFactory.getLogger(LeadSourceQueryService.class);

    private final LeadSourceRepository leadSourceRepository;

    private final LeadSourceMapper leadSourceMapper;

    public LeadSourceQueryService(LeadSourceRepository leadSourceRepository, LeadSourceMapper leadSourceMapper) {
        this.leadSourceRepository = leadSourceRepository;
        this.leadSourceMapper = leadSourceMapper;
    }

    /**
     * Return a {@link Page} of {@link LeadSourceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeadSourceDTO> findByCriteria(LeadSourceCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<LeadSource> specification = createSpecification(criteria);
        return leadSourceRepository.findAll(specification, page).map(leadSourceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeadSourceCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<LeadSource> specification = createSpecification(criteria);
        return leadSourceRepository.count(specification);
    }

    /**
     * Function to convert {@link LeadSourceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<LeadSource> createSpecification(LeadSourceCriteria criteria) {
        Specification<LeadSource> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), LeadSource_.id),
                buildStringSpecification(criteria.getName(), LeadSource_.name),
                buildStringSpecification(criteria.getNameSearch(), LeadSource_.nameSearch),
                buildSpecification(criteria.getIsActive(), LeadSource_.isActive),
                buildSpecification(criteria.getTenantId(), root -> root.join(LeadSource_.tenant, JoinType.LEFT).get(Tenant_.id))
            );
        }
        return specification;
    }
}
