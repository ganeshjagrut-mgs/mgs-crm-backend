package com.mgs.service;

import com.mgs.domain.*; // for static metamodels
import com.mgs.domain.PermissionModule;
import com.mgs.repository.PermissionModuleRepository;
import com.mgs.service.criteria.PermissionModuleCriteria;
import com.mgs.service.dto.PermissionModuleDTO;
import com.mgs.service.mapper.PermissionModuleMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link PermissionModule} entities in the database.
 * The main input is a {@link PermissionModuleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PermissionModuleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PermissionModuleQueryService extends QueryService<PermissionModule> {

    private static final Logger LOG = LoggerFactory.getLogger(PermissionModuleQueryService.class);

    private final PermissionModuleRepository permissionModuleRepository;

    private final PermissionModuleMapper permissionModuleMapper;

    public PermissionModuleQueryService(
        PermissionModuleRepository permissionModuleRepository,
        PermissionModuleMapper permissionModuleMapper
    ) {
        this.permissionModuleRepository = permissionModuleRepository;
        this.permissionModuleMapper = permissionModuleMapper;
    }

    /**
     * Return a {@link Page} of {@link PermissionModuleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PermissionModuleDTO> findByCriteria(PermissionModuleCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PermissionModule> specification = createSpecification(criteria);
        return permissionModuleRepository.findAll(specification, page).map(permissionModuleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PermissionModuleCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<PermissionModule> specification = createSpecification(criteria);
        return permissionModuleRepository.count(specification);
    }

    /**
     * Function to convert {@link PermissionModuleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PermissionModule> createSpecification(PermissionModuleCriteria criteria) {
        Specification<PermissionModule> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), PermissionModule_.id),
                buildStringSpecification(criteria.getName(), PermissionModule_.name),
                buildStringSpecification(criteria.getDescription(), PermissionModule_.description)
            );
        }
        return specification;
    }
}
