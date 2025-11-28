package com.mgs.service;

import com.mgs.domain.*; // for static metamodels
import com.mgs.domain.Pipeline;
import com.mgs.repository.PipelineRepository;
import com.mgs.service.criteria.PipelineCriteria;
import com.mgs.service.dto.PipelineDTO;
import com.mgs.service.mapper.PipelineMapper;
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
 * Service for executing complex queries for {@link Pipeline} entities in the database.
 * The main input is a {@link PipelineCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link PipelineDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PipelineQueryService extends QueryService<Pipeline> {

    private static final Logger LOG = LoggerFactory.getLogger(PipelineQueryService.class);

    private final PipelineRepository pipelineRepository;

    private final PipelineMapper pipelineMapper;

    public PipelineQueryService(PipelineRepository pipelineRepository, PipelineMapper pipelineMapper) {
        this.pipelineRepository = pipelineRepository;
        this.pipelineMapper = pipelineMapper;
    }

    /**
     * Return a {@link Page} of {@link PipelineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PipelineDTO> findByCriteria(PipelineCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Pipeline> specification = createSpecification(criteria);
        return pipelineRepository.findAll(specification, page).map(pipelineMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PipelineCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Pipeline> specification = createSpecification(criteria);
        return pipelineRepository.count(specification);
    }

    /**
     * Function to convert {@link PipelineCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Pipeline> createSpecification(PipelineCriteria criteria) {
        Specification<Pipeline> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Pipeline_.id),
                buildStringSpecification(criteria.getName(), Pipeline_.name),
                buildStringSpecification(criteria.getNameSearch(), Pipeline_.nameSearch),
                buildSpecification(criteria.getModule(), Pipeline_.module),
                buildSpecification(criteria.getIsDefault(), Pipeline_.isDefault),
                buildSpecification(criteria.getTenantId(), root -> root.join(Pipeline_.tenant, JoinType.LEFT).get(Tenant_.id))
            );
        }
        return specification;
    }
}
