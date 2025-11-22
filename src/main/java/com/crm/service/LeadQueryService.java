package com.crm.service;

import com.crm.domain.*; // for static metamodels
import com.crm.domain.Lead;
import com.crm.repository.LeadRepository;
import com.crm.service.criteria.LeadCriteria;
import com.crm.service.dto.LeadDTO;
import com.crm.service.mapper.LeadMapper;
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
 * Service for executing complex queries for {@link Lead} entities in the database.
 * The main input is a {@link LeadCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link LeadDTO} or a {@link Page} of {@link LeadDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class LeadQueryService extends QueryService<Lead> {

    private final Logger log = LoggerFactory.getLogger(LeadQueryService.class);

    private final LeadRepository leadRepository;

    private final LeadMapper leadMapper;

    public LeadQueryService(LeadRepository leadRepository, LeadMapper leadMapper) {
        this.leadRepository = leadRepository;
        this.leadMapper = leadMapper;
    }

    /**
     * Return a {@link List} of {@link LeadDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<LeadDTO> findByCriteria(LeadCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Lead> specification = createSpecification(criteria);
        return leadMapper.toDto(leadRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link LeadDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<LeadDTO> findByCriteria(LeadCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Lead> specification = createSpecification(criteria);
        return leadRepository.findAll(specification, page).map(leadMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(LeadCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Lead> specification = createSpecification(criteria);
        return leadRepository.count(specification);
    }

    /**
     * Function to convert {@link LeadCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Lead> createSpecification(LeadCriteria criteria) {
        Specification<Lead> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Lead_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Lead_.name));
            }
            if (criteria.getLeadNumber() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLeadNumber(), Lead_.leadNumber));
            }
            if (criteria.getAnnualRevenue() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAnnualRevenue(), Lead_.annualRevenue));
            }
            if (criteria.getUserId() != null) {
                specification =
                    specification.and(buildSpecification(criteria.getUserId(), root -> root.join(Lead_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getCustomerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCustomerId(), root -> root.join(Lead_.customer, JoinType.LEFT).get(Customer_.id))
                    );
            }
            if (criteria.getLeadSourceId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeadSourceId(),
                            root -> root.join(Lead_.leadSource, JoinType.LEFT).get(MasterStaticType_.id)
                        )
                    );
            }
            if (criteria.getIndustryTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getIndustryTypeId(),
                            root -> root.join(Lead_.industryType, JoinType.LEFT).get(MasterStaticType_.id)
                        )
                    );
            }
            if (criteria.getLeadStatusId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getLeadStatusId(),
                            root -> root.join(Lead_.leadStatus, JoinType.LEFT).get(MasterStaticType_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
