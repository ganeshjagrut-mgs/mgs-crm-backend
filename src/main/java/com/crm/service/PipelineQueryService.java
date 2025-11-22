package com.crm.service;

import com.crm.domain.*; // for static metamodels
import com.crm.domain.Pipeline;
import com.crm.repository.PipelineRepository;
import com.crm.service.criteria.PipelineCriteria;
import com.crm.service.dto.PipelineDTO;
import com.crm.service.mapper.PipelineMapper;
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
 * Service for executing complex queries for {@link Pipeline} entities in the database.
 * The main input is a {@link PipelineCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PipelineDTO} or a {@link Page} of {@link PipelineDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PipelineQueryService extends QueryService<Pipeline> {

    private final Logger log = LoggerFactory.getLogger(PipelineQueryService.class);

    private final PipelineRepository pipelineRepository;

    private final PipelineMapper pipelineMapper;

    public PipelineQueryService(PipelineRepository pipelineRepository, PipelineMapper pipelineMapper) {
        this.pipelineRepository = pipelineRepository;
        this.pipelineMapper = pipelineMapper;
    }

    /**
     * Return a {@link List} of {@link PipelineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PipelineDTO> findByCriteria(PipelineCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Pipeline> specification = createSpecification(criteria);
        return pipelineMapper.toDto(pipelineRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PipelineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PipelineDTO> findByCriteria(PipelineCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
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
        log.debug("count by criteria : {}", criteria);
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
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Pipeline_.id));
            }
            if (criteria.getPipelineName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPipelineName(), Pipeline_.pipelineName));
            }
            if (criteria.getTotalAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalAmount(), Pipeline_.totalAmount));
            }
            if (criteria.getNoOfSamples() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getNoOfSamples(), Pipeline_.noOfSamples));
            }
            if (criteria.getCorrelationId() != null) {
                specification = specification.and(buildSpecification(criteria.getCorrelationId(), Pipeline_.correlationId));
            }
            if (criteria.getPipelineTagsId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPipelineTagsId(),
                            root -> root.join(Pipeline_.pipelineTags, JoinType.LEFT).get(PipelineTag_.id)
                        )
                    );
            }
            if (criteria.getTasksId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getTasksId(), root -> root.join(Pipeline_.tasks, JoinType.LEFT).get(Task_.id))
                    );
            }
            if (criteria.getPipelineOwnerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPipelineOwnerId(),
                            root -> root.join(Pipeline_.pipelineOwner, JoinType.LEFT).get(User_.id)
                        )
                    );
            }
            if (criteria.getCustomerId() != null) {
                specification =
                    specification.and(
                        buildSpecification(criteria.getCustomerId(), root -> root.join(Pipeline_.customer, JoinType.LEFT).get(Customer_.id))
                    );
            }
            if (criteria.getStageOfPipelineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStageOfPipelineId(),
                            root -> root.join(Pipeline_.stageOfPipeline, JoinType.LEFT).get(MasterStaticType_.id)
                        )
                    );
            }
            if (criteria.getSubPipelineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSubPipelineId(),
                            root -> root.join(Pipeline_.subPipeline, JoinType.LEFT).get(SubPipeline_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
