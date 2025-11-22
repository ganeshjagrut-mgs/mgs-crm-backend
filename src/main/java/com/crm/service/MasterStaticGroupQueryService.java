package com.crm.service;

import com.crm.domain.*; // for static metamodels
import com.crm.domain.MasterStaticGroup;
import com.crm.repository.MasterStaticGroupRepository;
import com.crm.service.criteria.MasterStaticGroupCriteria;
import com.crm.service.dto.MasterStaticGroupDTO;
import com.crm.service.mapper.MasterStaticGroupMapper;
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
 * Service for executing complex queries for {@link MasterStaticGroup} entities in the database.
 * The main input is a {@link MasterStaticGroupCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link MasterStaticGroupDTO} or a {@link Page} of {@link MasterStaticGroupDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MasterStaticGroupQueryService extends QueryService<MasterStaticGroup> {

    private final Logger log = LoggerFactory.getLogger(MasterStaticGroupQueryService.class);

    private final MasterStaticGroupRepository masterStaticGroupRepository;

    private final MasterStaticGroupMapper masterStaticGroupMapper;

    public MasterStaticGroupQueryService(
        MasterStaticGroupRepository masterStaticGroupRepository,
        MasterStaticGroupMapper masterStaticGroupMapper
    ) {
        this.masterStaticGroupRepository = masterStaticGroupRepository;
        this.masterStaticGroupMapper = masterStaticGroupMapper;
    }

    /**
     * Return a {@link List} of {@link MasterStaticGroupDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<MasterStaticGroupDTO> findByCriteria(MasterStaticGroupCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<MasterStaticGroup> specification = createSpecification(criteria);
        return masterStaticGroupMapper.toDto(masterStaticGroupRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link MasterStaticGroupDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<MasterStaticGroupDTO> findByCriteria(MasterStaticGroupCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<MasterStaticGroup> specification = createSpecification(criteria);
        return masterStaticGroupRepository.findAll(specification, page).map(masterStaticGroupMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MasterStaticGroupCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<MasterStaticGroup> specification = createSpecification(criteria);
        return masterStaticGroupRepository.count(specification);
    }

    /**
     * Function to convert {@link MasterStaticGroupCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<MasterStaticGroup> createSpecification(MasterStaticGroupCriteria criteria) {
        Specification<MasterStaticGroup> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), MasterStaticGroup_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), MasterStaticGroup_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), MasterStaticGroup_.description));
            }
            if (criteria.getUiLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUiLabel(), MasterStaticGroup_.uiLabel));
            }
            if (criteria.getEditable() != null) {
                specification = specification.and(buildSpecification(criteria.getEditable(), MasterStaticGroup_.editable));
            }
            if (criteria.getEntityTypeId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getEntityTypeId(),
                            root -> root.join(MasterStaticGroup_.entityType, JoinType.LEFT).get(EntityType_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
