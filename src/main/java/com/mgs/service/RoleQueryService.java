package com.mgs.service;

import com.mgs.domain.*; // for static metamodels
import com.mgs.domain.Role;
import com.mgs.repository.RoleRepository;
import com.mgs.service.criteria.RoleCriteria;
import com.mgs.service.dto.RoleDTO;
import com.mgs.service.mapper.RoleMapper;
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
 * Service for executing complex queries for {@link Role} entities in the database.
 * The main input is a {@link RoleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link RoleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RoleQueryService extends QueryService<Role> {

    private static final Logger LOG = LoggerFactory.getLogger(RoleQueryService.class);

    private final RoleRepository roleRepository;

    private final RoleMapper roleMapper;

    public RoleQueryService(RoleRepository roleRepository, RoleMapper roleMapper) {
        this.roleRepository = roleRepository;
        this.roleMapper = roleMapper;
    }

    /**
     * Return a {@link Page} of {@link RoleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RoleDTO> findByCriteria(RoleCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Role> specification = createSpecification(criteria);
        return roleRepository.findAll(specification, page).map(roleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RoleCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Role> specification = createSpecification(criteria);
        return roleRepository.count(specification);
    }

    /**
     * Function to convert {@link RoleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Role> createSpecification(RoleCriteria criteria) {
        Specification<Role> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Role_.id),
                buildStringSpecification(criteria.getName(), Role_.name),
                buildStringSpecification(criteria.getCode(), Role_.code),
                buildSpecification(criteria.getIsSystem(), Role_.isSystem),
                buildRangeSpecification(criteria.getRoleLevel(), Role_.roleLevel),
                buildSpecification(criteria.getTenantId(), root -> root.join(Role_.tenant, JoinType.LEFT).get(Tenant_.id))
            );
        }
        return specification;
    }
}
