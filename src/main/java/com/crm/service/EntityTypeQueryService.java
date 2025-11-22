package com.crm.service;

import com.crm.domain.*; // for static metamodels
import com.crm.domain.EntityType;
import com.crm.repository.EntityTypeRepository;
import com.crm.service.criteria.EntityTypeCriteria;
import com.crm.service.dto.EntityTypeDTO;
import com.crm.service.mapper.EntityTypeMapper;
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
 * Service for executing complex queries for {@link EntityType} entities in the database.
 * The main input is a {@link EntityTypeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link EntityTypeDTO} or a {@link Page} of {@link EntityTypeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class EntityTypeQueryService extends QueryService<EntityType> {

    private final Logger log = LoggerFactory.getLogger(EntityTypeQueryService.class);

    private final EntityTypeRepository entityTypeRepository;

    private final EntityTypeMapper entityTypeMapper;

    public EntityTypeQueryService(EntityTypeRepository entityTypeRepository, EntityTypeMapper entityTypeMapper) {
        this.entityTypeRepository = entityTypeRepository;
        this.entityTypeMapper = entityTypeMapper;
    }

    /**
     * Return a {@link List} of {@link EntityTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<EntityTypeDTO> findByCriteria(EntityTypeCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<EntityType> specification = createSpecification(criteria);
        return entityTypeMapper.toDto(entityTypeRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link EntityTypeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<EntityTypeDTO> findByCriteria(EntityTypeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<EntityType> specification = createSpecification(criteria);
        return entityTypeRepository.findAll(specification, page).map(entityTypeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(EntityTypeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<EntityType> specification = createSpecification(criteria);
        return entityTypeRepository.count(specification);
    }

    /**
     * Function to convert {@link EntityTypeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<EntityType> createSpecification(EntityTypeCriteria criteria) {
        Specification<EntityType> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), EntityType_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), EntityType_.name));
            }
            if (criteria.getLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLabel(), EntityType_.label));
            }
        }
        return specification;
    }
}
