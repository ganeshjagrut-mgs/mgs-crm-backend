package com.mgs.service;

import com.mgs.domain.*; // for static metamodels
import com.mgs.domain.SubPipeline;
import com.mgs.repository.SubPipelineRepository;
import com.mgs.service.criteria.SubPipelineCriteria;
import com.mgs.service.dto.SubPipelineDTO;
import com.mgs.service.mapper.SubPipelineMapper;
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
 * Service for executing complex queries for {@link SubPipeline} entities in the database.
 * The main input is a {@link SubPipelineCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link SubPipelineDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SubPipelineQueryService extends QueryService<SubPipeline> {

    private static final Logger LOG = LoggerFactory.getLogger(SubPipelineQueryService.class);

    private final SubPipelineRepository subPipelineRepository;

    private final SubPipelineMapper subPipelineMapper;

    public SubPipelineQueryService(SubPipelineRepository subPipelineRepository, SubPipelineMapper subPipelineMapper) {
        this.subPipelineRepository = subPipelineRepository;
        this.subPipelineMapper = subPipelineMapper;
    }

    /**
     * Return a {@link Page} of {@link SubPipelineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SubPipelineDTO> findByCriteria(SubPipelineCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SubPipeline> specification = createSpecification(criteria);
        return subPipelineRepository.findAll(specification, page).map(subPipelineMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SubPipelineCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<SubPipeline> specification = createSpecification(criteria);
        return subPipelineRepository.count(specification);
    }

    /**
     * Function to convert {@link SubPipelineCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SubPipeline> createSpecification(SubPipelineCriteria criteria) {
        Specification<SubPipeline> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), SubPipeline_.id),
                buildStringSpecification(criteria.getName(), SubPipeline_.name),
                buildStringSpecification(criteria.getNameSearch(), SubPipeline_.nameSearch),
                buildRangeSpecification(criteria.getSequenceOrder(), SubPipeline_.sequenceOrder),
                buildRangeSpecification(criteria.getProbability(), SubPipeline_.probability),
                buildSpecification(criteria.getIsClosingStage(), SubPipeline_.isClosingStage),
                buildSpecification(criteria.getTenantId(), root -> root.join(SubPipeline_.tenant, JoinType.LEFT).get(Tenant_.id)),
                buildSpecification(criteria.getPipelineId(), root -> root.join(SubPipeline_.pipeline, JoinType.LEFT).get(Pipeline_.id))
            );
        }
        return specification;
    }
}
