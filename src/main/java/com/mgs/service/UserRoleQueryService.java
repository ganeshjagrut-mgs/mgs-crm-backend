package com.mgs.service;

import com.mgs.domain.*; // for static metamodels
import com.mgs.domain.UserRole;
import com.mgs.repository.UserRoleRepository;
import com.mgs.service.criteria.UserRoleCriteria;
import com.mgs.service.dto.UserRoleDTO;
import com.mgs.service.mapper.UserRoleMapper;
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
 * Service for executing complex queries for {@link UserRole} entities in the database.
 * The main input is a {@link UserRoleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link UserRoleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserRoleQueryService extends QueryService<UserRole> {

    private static final Logger LOG = LoggerFactory.getLogger(UserRoleQueryService.class);

    private final UserRoleRepository userRoleRepository;

    private final UserRoleMapper userRoleMapper;

    public UserRoleQueryService(UserRoleRepository userRoleRepository, UserRoleMapper userRoleMapper) {
        this.userRoleRepository = userRoleRepository;
        this.userRoleMapper = userRoleMapper;
    }

    /**
     * Return a {@link Page} of {@link UserRoleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserRoleDTO> findByCriteria(UserRoleCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserRole> specification = createSpecification(criteria);
        return userRoleRepository.findAll(specification, page).map(userRoleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserRoleCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<UserRole> specification = createSpecification(criteria);
        return userRoleRepository.count(specification);
    }

    /**
     * Function to convert {@link UserRoleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserRole> createSpecification(UserRoleCriteria criteria) {
        Specification<UserRole> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), UserRole_.id),
                buildSpecification(criteria.getTenantId(), root -> root.join(UserRole_.tenant, JoinType.LEFT).get(Tenant_.id)),
                buildSpecification(criteria.getUserId(), root -> root.join(UserRole_.user, JoinType.LEFT).get(User_.id)),
                buildSpecification(criteria.getRoleId(), root -> root.join(UserRole_.role, JoinType.LEFT).get(Role_.id))
            );
        }
        return specification;
    }
}
