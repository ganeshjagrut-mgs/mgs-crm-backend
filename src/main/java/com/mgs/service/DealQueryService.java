package com.mgs.service;

import com.mgs.domain.*; // for static metamodels
import com.mgs.domain.Deal;
import com.mgs.repository.DealRepository;
import com.mgs.service.criteria.DealCriteria;
import com.mgs.service.dto.DealDTO;
import com.mgs.service.mapper.DealMapper;
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
 * Service for executing complex queries for {@link Deal} entities in the database.
 * The main input is a {@link DealCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link DealDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class DealQueryService extends QueryService<Deal> {

    private static final Logger LOG = LoggerFactory.getLogger(DealQueryService.class);

    private final DealRepository dealRepository;

    private final DealMapper dealMapper;

    public DealQueryService(DealRepository dealRepository, DealMapper dealMapper) {
        this.dealRepository = dealRepository;
        this.dealMapper = dealMapper;
    }

    /**
     * Return a {@link Page} of {@link DealDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<DealDTO> findByCriteria(DealCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Deal> specification = createSpecification(criteria);
        return dealRepository.findAll(specification, page).map(dealMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(DealCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Deal> specification = createSpecification(criteria);
        return dealRepository.count(specification);
    }

    /**
     * Function to convert {@link DealCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Deal> createSpecification(DealCriteria criteria) {
        Specification<Deal> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Deal_.id),
                buildStringSpecification(criteria.getDealNumber(), Deal_.dealNumber),
                buildRangeSpecification(criteria.getDealValue(), Deal_.dealValue),
                buildSpecification(criteria.getStatus(), Deal_.status),
                buildStringSpecification(criteria.getCurrency(), Deal_.currency),
                buildRangeSpecification(criteria.getStartDate(), Deal_.startDate),
                buildRangeSpecification(criteria.getCloseDate(), Deal_.closeDate),
                buildStringSpecification(criteria.getNotes(), Deal_.notes),
                buildSpecification(criteria.getTenantId(), root -> root.join(Deal_.tenant, JoinType.LEFT).get(Tenant_.id)),
                buildSpecification(criteria.getCustomerId(), root -> root.join(Deal_.customer, JoinType.LEFT).get(Customer_.id)),
                buildSpecification(criteria.getContactId(), root -> root.join(Deal_.contact, JoinType.LEFT).get(Contact_.id)),
                buildSpecification(criteria.getLeadId(), root -> root.join(Deal_.lead, JoinType.LEFT).get(Lead_.id))
            );
        }
        return specification;
    }
}
