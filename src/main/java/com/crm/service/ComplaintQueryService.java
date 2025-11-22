package com.crm.service;

import com.crm.domain.*; // for static metamodels
import com.crm.domain.Complaint;
import com.crm.repository.ComplaintRepository;
import com.crm.service.criteria.ComplaintCriteria;
import com.crm.service.dto.ComplaintDTO;
import com.crm.service.mapper.ComplaintMapper;
import jakarta.persistence.criteria.JoinType;
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
 * Service for executing complex queries for {@link Complaint} entities in the database.
 * The main input is a {@link ComplaintCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link ComplaintDTO} or a {@link Page} of {@link ComplaintDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ComplaintQueryService extends QueryService<Complaint> {

    private final Logger log = LoggerFactory.getLogger(ComplaintQueryService.class);

    private final ComplaintRepository complaintRepository;

    private final ComplaintMapper complaintMapper;

    public ComplaintQueryService(ComplaintRepository complaintRepository, ComplaintMapper complaintMapper) {
        this.complaintRepository = complaintRepository;
        this.complaintMapper = complaintMapper;
    }

    /**
     * Return a {@link List} of {@link ComplaintDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<ComplaintDTO> findByCriteria(ComplaintCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Complaint> specification = createSpecification(criteria);
        return complaintMapper.toDto(complaintRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link ComplaintDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ComplaintDTO> findByCriteria(ComplaintCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
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
        log.debug("count by criteria : {}", criteria);
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
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Complaint_.id));
            }
            if (criteria.getComplaintNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getComplaintNumber(), Complaint_.complaintNumber));
            }
            if (criteria.getComplaintDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getComplaintDate(), Complaint_.complaintDate));
            }
            if (criteria.getRecordNumbers() != null) {
                specification = specification.and(buildStringSpecification(criteria.getRecordNumbers(), Complaint_.recordNumbers));
            }
            if (criteria.getCustomerContactNumber() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCustomerContactNumber(), Complaint_.customerContactNumber));
            }
            if (criteria.getCustomerContactEmail() != null) {
                specification =
                    specification.and(buildStringSpecification(criteria.getCustomerContactEmail(), Complaint_.customerContactEmail));
            }
            if (criteria.getExpectedClosureDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getExpectedClosureDate(), Complaint_.expectedClosureDate));
            }
            if (criteria.getComplaintStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getComplaintStatus(), Complaint_.complaintStatus));
            }
            if (criteria.getComplaintClosureDate() != null) {
                specification =
                    specification.and(buildRangeSpecification(criteria.getComplaintClosureDate(), Complaint_.complaintClosureDate));
            }
            if (criteria.getCustomerNameId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomerNameId(),
                            root -> root.join(Complaint_.customerName, JoinType.LEFT).get(Customer_.id)
                        )
                    );
            }
            if (criteria.getComplaintRelatedToId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getComplaintRelatedToId(),
                            root -> root.join(Complaint_.complaintRelatedTo, JoinType.LEFT).get(MasterStaticType_.id)
                        )
                    );
            }
            if (criteria.getTypeOfComplaintId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getTypeOfComplaintId(),
                            root -> root.join(Complaint_.typeOfComplaint, JoinType.LEFT).get(MasterStaticType_.id)
                        )
                    );
            }
            if (criteria.getComplaintRelatedPersonsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getComplaintRelatedPersonsId(),
                            root -> root.join(Complaint_.complaintRelatedPersons, JoinType.LEFT).get(User_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
