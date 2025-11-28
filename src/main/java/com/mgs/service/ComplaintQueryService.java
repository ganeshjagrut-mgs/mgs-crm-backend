package com.mgs.service;

import com.mgs.domain.*; // for static metamodels
import com.mgs.domain.Complaint;
import com.mgs.repository.ComplaintRepository;
import com.mgs.service.criteria.ComplaintCriteria;
import com.mgs.service.dto.ComplaintDTO;
import com.mgs.service.mapper.ComplaintMapper;
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
 * Service for executing complex queries for {@link Complaint} entities in the database.
 * The main input is a {@link ComplaintCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ComplaintDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ComplaintQueryService extends QueryService<Complaint> {

    private static final Logger LOG = LoggerFactory.getLogger(ComplaintQueryService.class);

    private final ComplaintRepository complaintRepository;

    private final ComplaintMapper complaintMapper;

    public ComplaintQueryService(ComplaintRepository complaintRepository, ComplaintMapper complaintMapper) {
        this.complaintRepository = complaintRepository;
        this.complaintMapper = complaintMapper;
    }

    /**
     * Return a {@link Page} of {@link ComplaintDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ComplaintDTO> findByCriteria(ComplaintCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Complaint> specification = createSpecification(criteria);
        return complaintRepository.findAll(specification, page).map(complaintMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ComplaintCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Complaint> specification = createSpecification(criteria);
        return complaintRepository.count(specification);
    }

    /**
     * Function to convert {@link ComplaintCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Complaint> createSpecification(ComplaintCriteria criteria) {
        Specification<Complaint> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Complaint_.id),
                buildStringSpecification(criteria.getComplaintNumber(), Complaint_.complaintNumber),
                buildStringSpecification(criteria.getSubject(), Complaint_.subject),
                buildStringSpecification(criteria.getDescription(), Complaint_.description),
                buildSpecification(criteria.getPriority(), Complaint_.priority),
                buildSpecification(criteria.getStatus(), Complaint_.status),
                buildSpecification(criteria.getSource(), Complaint_.source),
                buildSpecification(criteria.getTenantId(), root -> root.join(Complaint_.tenant, JoinType.LEFT).get(Tenant_.id)),
                buildSpecification(criteria.getCustomerId(), root -> root.join(Complaint_.customer, JoinType.LEFT).get(Customer_.id)),
                buildSpecification(criteria.getContactId(), root -> root.join(Complaint_.contact, JoinType.LEFT).get(Contact_.id)),
                buildSpecification(criteria.getCategoryId(), root ->
                    root.join(Complaint_.category, JoinType.LEFT).get(ComplaintCategory_.id)
                ),
                buildSpecification(criteria.getPipelineId(), root -> root.join(Complaint_.pipeline, JoinType.LEFT).get(Pipeline_.id)),
                buildSpecification(criteria.getStageId(), root -> root.join(Complaint_.stage, JoinType.LEFT).get(SubPipeline_.id)),
                buildSpecification(criteria.getAssignedDepartmentId(), root ->
                    root.join(Complaint_.assignedDepartment, JoinType.LEFT).get(Department_.id)
                ),
                buildSpecification(criteria.getAssignedUserId(), root -> root.join(Complaint_.assignedUser, JoinType.LEFT).get(User_.id))
            );
        }
        return specification;
    }
}
