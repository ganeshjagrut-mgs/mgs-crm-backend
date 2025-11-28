package com.mgs.service;

import com.mgs.domain.*; // for static metamodels
import com.mgs.domain.Lead;
import com.mgs.repository.LeadRepository;
import com.mgs.service.criteria.LeadCriteria;
import com.mgs.service.dto.LeadDTO;
import com.mgs.service.mapper.LeadMapper;
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
 * Service for executing complex queries for {@link Lead} entities in the database.
 * The main input is a {@link LeadCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link LeadDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeadQueryService extends QueryService<Lead> {

    private static final Logger LOG = LoggerFactory.getLogger(LeadQueryService.class);

    private final LeadRepository leadRepository;

    private final LeadMapper leadMapper;

    public LeadQueryService(LeadRepository leadRepository, LeadMapper leadMapper) {
        this.leadRepository = leadRepository;
        this.leadMapper = leadMapper;
    }

    /**
     * Return a {@link Page} of {@link LeadDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeadDTO> findByCriteria(LeadCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Lead> specification = createSpecification(criteria);
        return leadRepository.findAll(specification, page).map(leadMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeadCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Lead> specification = createSpecification(criteria);
        return leadRepository.count(specification);
    }

    /**
     * Function to convert {@link LeadCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Lead> createSpecification(LeadCriteria criteria) {
        Specification<Lead> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Lead_.id),
                buildStringSpecification(criteria.getTitle(), Lead_.title),
                buildSpecification(criteria.getStatus(), Lead_.status),
                buildRangeSpecification(criteria.getEstimatedValue(), Lead_.estimatedValue),
                buildStringSpecification(criteria.getCurrency(), Lead_.currency),
                buildStringSpecification(criteria.getNotes(), Lead_.notes),
                buildSpecification(criteria.getTenantId(), root -> root.join(Lead_.tenant, JoinType.LEFT).get(Tenant_.id)),
                buildSpecification(criteria.getCustomerId(), root -> root.join(Lead_.customer, JoinType.LEFT).get(Customer_.id)),
                buildSpecification(criteria.getContactId(), root -> root.join(Lead_.contact, JoinType.LEFT).get(Contact_.id)),
                buildSpecification(criteria.getSourceId(), root -> root.join(Lead_.source, JoinType.LEFT).get(LeadSource_.id)),
                buildSpecification(criteria.getPipelineId(), root -> root.join(Lead_.pipeline, JoinType.LEFT).get(Pipeline_.id)),
                buildSpecification(criteria.getStageId(), root -> root.join(Lead_.stage, JoinType.LEFT).get(SubPipeline_.id)),
                buildSpecification(criteria.getOwnerUserId(), root -> root.join(Lead_.ownerUser, JoinType.LEFT).get(User_.id))
            );
        }
        return specification;
    }
}
