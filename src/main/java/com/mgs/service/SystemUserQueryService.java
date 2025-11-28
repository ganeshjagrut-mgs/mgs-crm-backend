package com.mgs.service;

import com.mgs.domain.*; // for static metamodels
import com.mgs.domain.SystemUser;
import com.mgs.repository.SystemUserRepository;
import com.mgs.service.criteria.SystemUserCriteria;
import com.mgs.service.dto.SystemUserDTO;
import com.mgs.service.mapper.SystemUserMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link SystemUser} entities in the database.
 * The main input is a {@link SystemUserCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SystemUserDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SystemUserQueryService extends QueryService<SystemUser> {

    private static final Logger LOG = LoggerFactory.getLogger(SystemUserQueryService.class);

    private final SystemUserRepository systemUserRepository;

    private final SystemUserMapper systemUserMapper;

    public SystemUserQueryService(SystemUserRepository systemUserRepository, SystemUserMapper systemUserMapper) {
        this.systemUserRepository = systemUserRepository;
        this.systemUserMapper = systemUserMapper;
    }

    /**
     * Return a {@link Page} of {@link SystemUserDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SystemUserDTO> findByCriteria(SystemUserCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SystemUser> specification = createSpecification(criteria);
        return systemUserRepository.findAll(specification, page).map(systemUserMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SystemUserCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SystemUser> specification = createSpecification(criteria);
        return systemUserRepository.count(specification);
    }

    /**
     * Function to convert {@link SystemUserCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SystemUser> createSpecification(SystemUserCriteria criteria) {
        Specification<SystemUser> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), SystemUser_.id),
                buildStringSpecification(criteria.getEmail(), SystemUser_.email),
                buildStringSpecification(criteria.getPasswordHash(), SystemUser_.passwordHash),
                buildSpecification(criteria.getIsSuperAdmin(), SystemUser_.isSuperAdmin)
            );
        }
        return specification;
    }
}
