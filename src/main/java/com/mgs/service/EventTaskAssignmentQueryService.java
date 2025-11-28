package com.mgs.service;

import com.mgs.domain.*; // for static metamodels
import com.mgs.domain.EventTaskAssignment;
import com.mgs.repository.EventTaskAssignmentRepository;
import com.mgs.service.criteria.EventTaskAssignmentCriteria;
import com.mgs.service.dto.EventTaskAssignmentDTO;
import com.mgs.service.mapper.EventTaskAssignmentMapper;
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
 * Service for executing complex queries for {@link EventTaskAssignment} entities in the database.
 * The main input is a {@link EventTaskAssignmentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link EventTaskAssignmentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EventTaskAssignmentQueryService extends QueryService<EventTaskAssignment> {

    private static final Logger LOG = LoggerFactory.getLogger(EventTaskAssignmentQueryService.class);

    private final EventTaskAssignmentRepository eventTaskAssignmentRepository;

    private final EventTaskAssignmentMapper eventTaskAssignmentMapper;

    public EventTaskAssignmentQueryService(
        EventTaskAssignmentRepository eventTaskAssignmentRepository,
        EventTaskAssignmentMapper eventTaskAssignmentMapper
    ) {
        this.eventTaskAssignmentRepository = eventTaskAssignmentRepository;
        this.eventTaskAssignmentMapper = eventTaskAssignmentMapper;
    }

    /**
     * Return a {@link Page} of {@link EventTaskAssignmentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EventTaskAssignmentDTO> findByCriteria(EventTaskAssignmentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EventTaskAssignment> specification = createSpecification(criteria);
        return eventTaskAssignmentRepository.findAll(specification, page).map(eventTaskAssignmentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EventTaskAssignmentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<EventTaskAssignment> specification = createSpecification(criteria);
        return eventTaskAssignmentRepository.count(specification);
    }

    /**
     * Function to convert {@link EventTaskAssignmentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EventTaskAssignment> createSpecification(EventTaskAssignmentCriteria criteria) {
        Specification<EventTaskAssignment> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), EventTaskAssignment_.id),
                buildSpecification(criteria.getEventId(), root -> root.join(EventTaskAssignment_.event, JoinType.LEFT).get(Event_.id)),
                buildSpecification(criteria.getTaskId(), root -> root.join(EventTaskAssignment_.task, JoinType.LEFT).get(Task_.id)),
                buildSpecification(criteria.getAssignedToId(), root ->
                    root.join(EventTaskAssignment_.assignedTo, JoinType.LEFT).get(User_.id)
                )
            );
        }
        return specification;
    }
}
