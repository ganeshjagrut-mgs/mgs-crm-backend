package com.crm.service;

import com.crm.domain.*; // for static metamodels
import com.crm.domain.SubPipelineCloseStage;
import com.crm.repository.SubPipelineCloseStageRepository;
import com.crm.service.criteria.SubPipelineCloseStageCriteria;
import com.crm.service.dto.SubPipelineCloseStageDTO;
import com.crm.service.mapper.SubPipelineCloseStageMapper;
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
 * Service for executing complex queries for {@link SubPipelineCloseStage} entities in the database.
 * The main input is a {@link SubPipelineCloseStageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SubPipelineCloseStageDTO} or a {@link Page} of {@link SubPipelineCloseStageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SubPipelineCloseStageQueryService extends QueryService<SubPipelineCloseStage> {

    private final Logger log = LoggerFactory.getLogger(SubPipelineCloseStageQueryService.class);

    private final SubPipelineCloseStageRepository subPipelineCloseStageRepository;

    private final SubPipelineCloseStageMapper subPipelineCloseStageMapper;

    public SubPipelineCloseStageQueryService(
        SubPipelineCloseStageRepository subPipelineCloseStageRepository,
        SubPipelineCloseStageMapper subPipelineCloseStageMapper
    ) {
        this.subPipelineCloseStageRepository = subPipelineCloseStageRepository;
        this.subPipelineCloseStageMapper = subPipelineCloseStageMapper;
    }

    /**
     * Return a {@link List} of {@link SubPipelineCloseStageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SubPipelineCloseStageDTO> findByCriteria(SubPipelineCloseStageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SubPipelineCloseStage> specification = createSpecification(criteria);
        return subPipelineCloseStageMapper.toDto(subPipelineCloseStageRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SubPipelineCloseStageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SubPipelineCloseStageDTO> findByCriteria(SubPipelineCloseStageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SubPipelineCloseStage> specification = createSpecification(criteria);
        return subPipelineCloseStageRepository.findAll(specification, page).map(subPipelineCloseStageMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SubPipelineCloseStageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SubPipelineCloseStage> specification = createSpecification(criteria);
        return subPipelineCloseStageRepository.count(specification);
    }

    /**
     * Function to convert {@link SubPipelineCloseStageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SubPipelineCloseStage> createSpecification(SubPipelineCloseStageCriteria criteria) {
        Specification<SubPipelineCloseStage> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SubPipelineCloseStage_.id));
            }
            if (criteria.getIndex() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIndex(), SubPipelineCloseStage_.index));
            }
            if (criteria.getStageId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStageId(),
                            root -> root.join(SubPipelineCloseStage_.stage, JoinType.LEFT).get(MasterStaticType_.id)
                        )
                    );
            }
            if (criteria.getSubPipelineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSubPipelineId(),
                            root -> root.join(SubPipelineCloseStage_.subPipeline, JoinType.LEFT).get(SubPipeline_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
