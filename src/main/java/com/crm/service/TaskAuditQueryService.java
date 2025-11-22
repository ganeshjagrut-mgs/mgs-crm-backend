package com.crm.service;

import com.crm.domain.*; // for static metamodels
import com.crm.domain.TaskAudit;
import com.crm.repository.TaskAuditRepository;
import com.crm.service.criteria.TaskAuditCriteria;
import com.crm.service.dto.TaskAuditDTO;
import com.crm.service.mapper.TaskAuditMapper;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link TaskAudit} entities in the database.
 * The main input is a {@link TaskAuditCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link TaskAuditDTO} or a {@link Page} of {@link TaskAuditDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TaskAuditQueryService extends QueryService<TaskAudit> {

    private final Logger log = LoggerFactory.getLogger(TaskAuditQueryService.class);

    private final TaskAuditRepository taskAuditRepository;

    private final TaskAuditMapper taskAuditMapper;

    public TaskAuditQueryService(TaskAuditRepository taskAuditRepository, TaskAuditMapper taskAuditMapper) {
        this.taskAuditRepository = taskAuditRepository;
        this.taskAuditMapper = taskAuditMapper;
    }

    /**
     * Return a {@link List} of {@link TaskAuditDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<TaskAuditDTO> findByCriteria(TaskAuditCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<TaskAudit> specification = createSpecification(criteria);
        return taskAuditMapper.toDto(taskAuditRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link TaskAuditDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TaskAuditDTO> findByCriteria(TaskAuditCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TaskAudit> specification = createSpecification(criteria);
        return taskAuditRepository.findAll(specification, page).map(taskAuditMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TaskAuditCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<TaskAudit> specification = createSpecification(criteria);
        return taskAuditRepository.count(specification);
    }

    /**
     * Function to convert {@link TaskAuditCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TaskAudit> createSpecification(TaskAuditCriteria criteria) {
        Specification<TaskAudit> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), TaskAudit_.id));
            }
            if (criteria.getEventTimestamp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEventTimestamp(), TaskAudit_.eventTimestamp));
            }
            if (criteria.getAction() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAction(), TaskAudit_.action));
            }
            if (criteria.getRowId() != null) {
                specification = specification.and(buildSpecification(criteria.getRowId(), TaskAudit_.rowId));
            }
            if (criteria.getCorrelationId() != null) {
                specification = specification.and(buildSpecification(criteria.getCorrelationId(), TaskAudit_.correlationId));
            }
        }
        return specification;
    }
}
