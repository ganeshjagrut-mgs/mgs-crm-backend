package com.mgs.service;

import com.mgs.domain.*; // for static metamodels
import com.mgs.domain.UserHierarchy;
import com.mgs.repository.UserHierarchyRepository;
import com.mgs.service.criteria.UserHierarchyCriteria;
import com.mgs.service.dto.UserHierarchyDTO;
import com.mgs.service.mapper.UserHierarchyMapper;
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
 * Service for executing complex queries for {@link UserHierarchy} entities in the database.
 * The main input is a {@link UserHierarchyCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link UserHierarchyDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserHierarchyQueryService extends QueryService<UserHierarchy> {

    private static final Logger LOG = LoggerFactory.getLogger(UserHierarchyQueryService.class);

    private final UserHierarchyRepository userHierarchyRepository;

    private final UserHierarchyMapper userHierarchyMapper;

    public UserHierarchyQueryService(UserHierarchyRepository userHierarchyRepository, UserHierarchyMapper userHierarchyMapper) {
        this.userHierarchyRepository = userHierarchyRepository;
        this.userHierarchyMapper = userHierarchyMapper;
    }

    /**
     * Return a {@link Page} of {@link UserHierarchyDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserHierarchyDTO> findByCriteria(UserHierarchyCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserHierarchy> specification = createSpecification(criteria);
        return userHierarchyRepository.findAll(specification, page).map(userHierarchyMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserHierarchyCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<UserHierarchy> specification = createSpecification(criteria);
        return userHierarchyRepository.count(specification);
    }

    /**
     * Function to convert {@link UserHierarchyCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserHierarchy> createSpecification(UserHierarchyCriteria criteria) {
        Specification<UserHierarchy> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), UserHierarchy_.id),
                buildStringSpecification(criteria.getRelationshipType(), UserHierarchy_.relationshipType),
                buildSpecification(criteria.getTenantId(), root -> root.join(UserHierarchy_.tenant, JoinType.LEFT).get(Tenant_.id)),
                buildSpecification(criteria.getParentUserId(), root -> root.join(UserHierarchy_.parentUser, JoinType.LEFT).get(User_.id)),
                buildSpecification(criteria.getChildUserId(), root -> root.join(UserHierarchy_.childUser, JoinType.LEFT).get(User_.id))
            );
        }
        return specification;
    }
}
