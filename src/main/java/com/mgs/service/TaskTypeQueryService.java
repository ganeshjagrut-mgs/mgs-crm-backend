package com.mgs.service;

import com.mgs.domain.*; // for static metamodels
import com.mgs.domain.TaskType;
import com.mgs.repository.TaskTypeRepository;
import com.mgs.service.criteria.TaskTypeCriteria;
import com.mgs.service.dto.TaskTypeDTO;
import com.mgs.service.mapper.TaskTypeMapper;
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
 * Service for executing complex queries for {@link TaskType} entities in the database.
 * The main input is a {@link TaskTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TaskTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TaskTypeQueryService extends QueryService<TaskType> {

    private static final Logger LOG = LoggerFactory.getLogger(TaskTypeQueryService.class);

    private final TaskTypeRepository taskTypeRepository;

    private final TaskTypeMapper taskTypeMapper;

    public TaskTypeQueryService(TaskTypeRepository taskTypeRepository, TaskTypeMapper taskTypeMapper) {
        this.taskTypeRepository = taskTypeRepository;
        this.taskTypeMapper = taskTypeMapper;
    }

    /**
     * Return a {@link Page} of {@link TaskTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TaskTypeDTO> findByCriteria(TaskTypeCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TaskType> specification = createSpecification(criteria);
        return taskTypeRepository.findAll(specification, page).map(taskTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TaskTypeCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TaskType> specification = createSpecification(criteria);
        return taskTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link TaskTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TaskType> createSpecification(TaskTypeCriteria criteria) {
        Specification<TaskType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), TaskType_.id),
                buildStringSpecification(criteria.getName(), TaskType_.name),
                buildSpecification(criteria.getIsActive(), TaskType_.isActive),
                buildSpecification(criteria.getTenantId(), root -> root.join(TaskType_.tenant, JoinType.LEFT).get(Tenant_.id))
            );
        }
        return specification;
    }
}
