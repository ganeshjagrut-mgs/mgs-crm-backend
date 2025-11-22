package com.crm.service;

import com.crm.domain.*; // for static metamodels
import com.crm.domain.SubPipeline;
import com.crm.repository.SubPipelineRepository;
import com.crm.service.criteria.SubPipelineCriteria;
import com.crm.service.dto.SubPipelineDTO;
import com.crm.service.mapper.SubPipelineMapper;
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
 * Service for executing complex queries for {@link SubPipeline} entities in the database.
 * The main input is a {@link SubPipelineCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SubPipelineDTO} or a {@link Page} of {@link SubPipelineDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SubPipelineQueryService extends QueryService<SubPipeline> {

    private final Logger log = LoggerFactory.getLogger(SubPipelineQueryService.class);

    private final SubPipelineRepository subPipelineRepository;

    private final SubPipelineMapper subPipelineMapper;

    public SubPipelineQueryService(SubPipelineRepository subPipelineRepository, SubPipelineMapper subPipelineMapper) {
        this.subPipelineRepository = subPipelineRepository;
        this.subPipelineMapper = subPipelineMapper;
    }

    /**
     * Return a {@link List} of {@link SubPipelineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SubPipelineDTO> findByCriteria(SubPipelineCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SubPipeline> specification = createSpecification(criteria);
        return subPipelineMapper.toDto(subPipelineRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SubPipelineDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SubPipelineDTO> findByCriteria(SubPipelineCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
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
        log.debug("count by criteria : {}", criteria);
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
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SubPipeline_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), SubPipeline_.name));
            }
            if (criteria.getIndex() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIndex(), SubPipeline_.index));
            }
            if (criteria.getOpenStagesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getOpenStagesId(),
                            root -> root.join(SubPipeline_.openStages, JoinType.LEFT).get(SubPipelineOpenStage_.id)
                        )
                    );
            }
            if (criteria.getCloseStagesId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getCloseStagesId(),
                            root -> root.join(SubPipeline_.closeStages, JoinType.LEFT).get(SubPipelineCloseStage_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
