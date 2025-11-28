package com.mgs.service;

import com.mgs.domain.*; // for static metamodels
import com.mgs.domain.RolePermission;
import com.mgs.repository.RolePermissionRepository;
import com.mgs.service.criteria.RolePermissionCriteria;
import com.mgs.service.dto.RolePermissionDTO;
import com.mgs.service.mapper.RolePermissionMapper;
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
 * Service for executing complex queries for {@link RolePermission} entities in the database.
 * The main input is a {@link RolePermissionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link RolePermissionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class RolePermissionQueryService extends QueryService<RolePermission> {

    private static final Logger LOG = LoggerFactory.getLogger(RolePermissionQueryService.class);

    private final RolePermissionRepository rolePermissionRepository;

    private final RolePermissionMapper rolePermissionMapper;

    public RolePermissionQueryService(RolePermissionRepository rolePermissionRepository, RolePermissionMapper rolePermissionMapper) {
        this.rolePermissionRepository = rolePermissionRepository;
        this.rolePermissionMapper = rolePermissionMapper;
    }

    /**
     * Return a {@link Page} of {@link RolePermissionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<RolePermissionDTO> findByCriteria(RolePermissionCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<RolePermission> specification = createSpecification(criteria);
        return rolePermissionRepository.findAll(specification, page).map(rolePermissionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(RolePermissionCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<RolePermission> specification = createSpecification(criteria);
        return rolePermissionRepository.count(specification);
    }

    /**
     * Function to convert {@link RolePermissionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<RolePermission> createSpecification(RolePermissionCriteria criteria) {
        Specification<RolePermission> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), RolePermission_.id),
                buildSpecification(criteria.getCanRead(), RolePermission_.canRead),
                buildSpecification(criteria.getCanCreate(), RolePermission_.canCreate),
                buildSpecification(criteria.getCanUpdate(), RolePermission_.canUpdate),
                buildSpecification(criteria.getCanDelete(), RolePermission_.canDelete),
                buildSpecification(criteria.getRoleId(), root -> root.join(RolePermission_.role, JoinType.LEFT).get(Role_.id)),
                buildSpecification(criteria.getModuleId(), root ->
                    root.join(RolePermission_.module, JoinType.LEFT).get(PermissionModule_.id)
                )
            );
        }
        return specification;
    }
}
