package com.mgs.service;

import com.mgs.domain.*; // for static metamodels
import com.mgs.domain.UserDepartment;
import com.mgs.repository.UserDepartmentRepository;
import com.mgs.service.criteria.UserDepartmentCriteria;
import com.mgs.service.dto.UserDepartmentDTO;
import com.mgs.service.mapper.UserDepartmentMapper;
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
 * Service for executing complex queries for {@link UserDepartment} entities in the database.
 * The main input is a {@link UserDepartmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link UserDepartmentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserDepartmentQueryService extends QueryService<UserDepartment> {

    private static final Logger LOG = LoggerFactory.getLogger(UserDepartmentQueryService.class);

    private final UserDepartmentRepository userDepartmentRepository;

    private final UserDepartmentMapper userDepartmentMapper;

    public UserDepartmentQueryService(UserDepartmentRepository userDepartmentRepository, UserDepartmentMapper userDepartmentMapper) {
        this.userDepartmentRepository = userDepartmentRepository;
        this.userDepartmentMapper = userDepartmentMapper;
    }

    /**
     * Return a {@link Page} of {@link UserDepartmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserDepartmentDTO> findByCriteria(UserDepartmentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserDepartment> specification = createSpecification(criteria);
        return userDepartmentRepository.findAll(specification, page).map(userDepartmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserDepartmentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<UserDepartment> specification = createSpecification(criteria);
        return userDepartmentRepository.count(specification);
    }

    /**
     * Function to convert {@link UserDepartmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserDepartment> createSpecification(UserDepartmentCriteria criteria) {
        Specification<UserDepartment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), UserDepartment_.id),
                buildSpecification(criteria.getTenantId(), root -> root.join(UserDepartment_.tenant, JoinType.LEFT).get(Tenant_.id)),
                buildSpecification(criteria.getUserId(), root -> root.join(UserDepartment_.user, JoinType.LEFT).get(User_.id)),
                buildSpecification(criteria.getDepartmentId(), root ->
                    root.join(UserDepartment_.department, JoinType.LEFT).get(Department_.id)
                )
            );
        }
        return specification;
    }
}
