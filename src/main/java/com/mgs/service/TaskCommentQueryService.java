package com.mgs.service;

import com.mgs.domain.*; // for static metamodels
import com.mgs.domain.TaskComment;
import com.mgs.repository.TaskCommentRepository;
import com.mgs.service.criteria.TaskCommentCriteria;
import com.mgs.service.dto.TaskCommentDTO;
import com.mgs.service.mapper.TaskCommentMapper;
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
 * Service for executing complex queries for {@link TaskComment} entities in the database.
 * The main input is a {@link TaskCommentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link TaskCommentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class TaskCommentQueryService extends QueryService<TaskComment> {

    private static final Logger LOG = LoggerFactory.getLogger(TaskCommentQueryService.class);

    private final TaskCommentRepository taskCommentRepository;

    private final TaskCommentMapper taskCommentMapper;

    public TaskCommentQueryService(TaskCommentRepository taskCommentRepository, TaskCommentMapper taskCommentMapper) {
        this.taskCommentRepository = taskCommentRepository;
        this.taskCommentMapper = taskCommentMapper;
    }

    /**
     * Return a {@link Page} of {@link TaskCommentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<TaskCommentDTO> findByCriteria(TaskCommentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<TaskComment> specification = createSpecification(criteria);
        return taskCommentRepository.findAll(specification, page).map(taskCommentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(TaskCommentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<TaskComment> specification = createSpecification(criteria);
        return taskCommentRepository.count(specification);
    }

    /**
     * Function to convert {@link TaskCommentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<TaskComment> createSpecification(TaskCommentCriteria criteria) {
        Specification<TaskComment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), TaskComment_.id),
                buildStringSpecification(criteria.getComment(), TaskComment_.comment),
                buildSpecification(criteria.getTaskId(), root -> root.join(TaskComment_.task, JoinType.LEFT).get(Task_.id)),
                buildSpecification(criteria.getCreatedByUserId(), root -> root.join(TaskComment_.createdByUser, JoinType.LEFT).get(User_.id)
                )
            );
        }
        return specification;
    }
}
