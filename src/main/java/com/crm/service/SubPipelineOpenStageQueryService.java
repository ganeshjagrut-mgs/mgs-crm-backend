package com.crm.service;

import com.crm.domain.*; // for static metamodels
import com.crm.domain.SubPipelineOpenStage;
import com.crm.repository.SubPipelineOpenStageRepository;
import com.crm.service.criteria.SubPipelineOpenStageCriteria;
import com.crm.service.dto.SubPipelineOpenStageDTO;
import com.crm.service.mapper.SubPipelineOpenStageMapper;
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
 * Service for executing complex queries for {@link SubPipelineOpenStage} entities in the database.
 * The main input is a {@link SubPipelineOpenStageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link SubPipelineOpenStageDTO} or a {@link Page} of {@link SubPipelineOpenStageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class SubPipelineOpenStageQueryService extends QueryService<SubPipelineOpenStage> {

    private final Logger log = LoggerFactory.getLogger(SubPipelineOpenStageQueryService.class);

    private final SubPipelineOpenStageRepository subPipelineOpenStageRepository;

    private final SubPipelineOpenStageMapper subPipelineOpenStageMapper;

    public SubPipelineOpenStageQueryService(
        SubPipelineOpenStageRepository subPipelineOpenStageRepository,
        SubPipelineOpenStageMapper subPipelineOpenStageMapper
    ) {
        this.subPipelineOpenStageRepository = subPipelineOpenStageRepository;
        this.subPipelineOpenStageMapper = subPipelineOpenStageMapper;
    }

    /**
     * Return a {@link List} of {@link SubPipelineOpenStageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<SubPipelineOpenStageDTO> findByCriteria(SubPipelineOpenStageCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<SubPipelineOpenStage> specification = createSpecification(criteria);
        return subPipelineOpenStageMapper.toDto(subPipelineOpenStageRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link SubPipelineOpenStageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<SubPipelineOpenStageDTO> findByCriteria(SubPipelineOpenStageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<SubPipelineOpenStage> specification = createSpecification(criteria);
        return subPipelineOpenStageRepository.findAll(specification, page).map(subPipelineOpenStageMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(SubPipelineOpenStageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<SubPipelineOpenStage> specification = createSpecification(criteria);
        return subPipelineOpenStageRepository.count(specification);
    }

    /**
     * Function to convert {@link SubPipelineOpenStageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<SubPipelineOpenStage> createSpecification(SubPipelineOpenStageCriteria criteria) {
        Specification<SubPipelineOpenStage> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), SubPipelineOpenStage_.id));
            }
            if (criteria.getIndex() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getIndex(), SubPipelineOpenStage_.index));
            }
            if (criteria.getStageId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getStageId(),
                            root -> root.join(SubPipelineOpenStage_.stage, JoinType.LEFT).get(MasterStaticType_.id)
                        )
                    );
            }
            if (criteria.getSubPipelineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getSubPipelineId(),
                            root -> root.join(SubPipelineOpenStage_.subPipeline, JoinType.LEFT).get(SubPipeline_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
