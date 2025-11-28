package com.mgs.service;

import com.mgs.domain.*; // for static metamodels
import com.mgs.domain.ComplaintCategory;
import com.mgs.repository.ComplaintCategoryRepository;
import com.mgs.service.criteria.ComplaintCategoryCriteria;
import com.mgs.service.dto.ComplaintCategoryDTO;
import com.mgs.service.mapper.ComplaintCategoryMapper;
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
 * Service for executing complex queries for {@link ComplaintCategory} entities in the database.
 * The main input is a {@link ComplaintCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ComplaintCategoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ComplaintCategoryQueryService extends QueryService<ComplaintCategory> {

    private static final Logger LOG = LoggerFactory.getLogger(ComplaintCategoryQueryService.class);

    private final ComplaintCategoryRepository complaintCategoryRepository;

    private final ComplaintCategoryMapper complaintCategoryMapper;

    public ComplaintCategoryQueryService(
        ComplaintCategoryRepository complaintCategoryRepository,
        ComplaintCategoryMapper complaintCategoryMapper
    ) {
        this.complaintCategoryRepository = complaintCategoryRepository;
        this.complaintCategoryMapper = complaintCategoryMapper;
    }

    /**
     * Return a {@link Page} of {@link ComplaintCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ComplaintCategoryDTO> findByCriteria(ComplaintCategoryCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ComplaintCategory> specification = createSpecification(criteria);
        return complaintCategoryRepository.findAll(specification, page).map(complaintCategoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ComplaintCategoryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ComplaintCategory> specification = createSpecification(criteria);
        return complaintCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link ComplaintCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ComplaintCategory> createSpecification(ComplaintCategoryCriteria criteria) {
        Specification<ComplaintCategory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ComplaintCategory_.id),
                buildStringSpecification(criteria.getName(), ComplaintCategory_.name),
                buildSpecification(criteria.getIsActive(), ComplaintCategory_.isActive),
                buildSpecification(criteria.getTenantId(), root -> root.join(ComplaintCategory_.tenant, JoinType.LEFT).get(Tenant_.id))
            );
        }
        return specification;
    }
}
