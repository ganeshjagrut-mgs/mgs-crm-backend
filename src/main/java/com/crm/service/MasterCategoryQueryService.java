package com.crm.service;

import com.crm.domain.*; // for static metamodels
import com.crm.domain.MasterCategory;
import com.crm.repository.MasterCategoryRepository;
import com.crm.service.criteria.MasterCategoryCriteria;
import com.crm.service.dto.MasterCategoryDTO;
import com.crm.service.mapper.MasterCategoryMapper;
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
 * Service for executing complex queries for {@link MasterCategory} entities in the database.
 * The main input is a {@link MasterCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MasterCategoryDTO} or a {@link Page} of {@link MasterCategoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MasterCategoryQueryService extends QueryService<MasterCategory> {

    private final Logger log = LoggerFactory.getLogger(MasterCategoryQueryService.class);

    private final MasterCategoryRepository masterCategoryRepository;

    private final MasterCategoryMapper masterCategoryMapper;

    public MasterCategoryQueryService(MasterCategoryRepository masterCategoryRepository, MasterCategoryMapper masterCategoryMapper) {
        this.masterCategoryRepository = masterCategoryRepository;
        this.masterCategoryMapper = masterCategoryMapper;
    }

    /**
     * Return a {@link List} of {@link MasterCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MasterCategoryDTO> findByCriteria(MasterCategoryCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MasterCategory> specification = createSpecification(criteria);
        return masterCategoryMapper.toDto(masterCategoryRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MasterCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MasterCategoryDTO> findByCriteria(MasterCategoryCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MasterCategory> specification = createSpecification(criteria);
        return masterCategoryRepository.findAll(specification, page).map(masterCategoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MasterCategoryCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MasterCategory> specification = createSpecification(criteria);
        return masterCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link MasterCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MasterCategory> createSpecification(MasterCategoryCriteria criteria) {
        Specification<MasterCategory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MasterCategory_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), MasterCategory_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), MasterCategory_.description));
            }
            if (criteria.getCode() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCode(), MasterCategory_.code));
            }
            if (criteria.getCustomersId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCustomersId(),
                            root -> root.join(MasterCategory_.customers, JoinType.LEFT).get(Customer_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
