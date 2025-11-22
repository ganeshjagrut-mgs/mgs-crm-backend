package com.crm.service;

import com.crm.domain.*; // for static metamodels
import com.crm.domain.PipelineTag;
import com.crm.repository.PipelineTagRepository;
import com.crm.service.criteria.PipelineTagCriteria;
import com.crm.service.dto.PipelineTagDTO;
import com.crm.service.mapper.PipelineTagMapper;
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
 * Service for executing complex queries for {@link PipelineTag} entities in the database.
 * The main input is a {@link PipelineTagCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PipelineTagDTO} or a {@link Page} of {@link PipelineTagDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PipelineTagQueryService extends QueryService<PipelineTag> {

    private final Logger log = LoggerFactory.getLogger(PipelineTagQueryService.class);

    private final PipelineTagRepository pipelineTagRepository;

    private final PipelineTagMapper pipelineTagMapper;

    public PipelineTagQueryService(PipelineTagRepository pipelineTagRepository, PipelineTagMapper pipelineTagMapper) {
        this.pipelineTagRepository = pipelineTagRepository;
        this.pipelineTagMapper = pipelineTagMapper;
    }

    /**
     * Return a {@link List} of {@link PipelineTagDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PipelineTagDTO> findByCriteria(PipelineTagCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PipelineTag> specification = createSpecification(criteria);
        return pipelineTagMapper.toDto(pipelineTagRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PipelineTagDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PipelineTagDTO> findByCriteria(PipelineTagCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PipelineTag> specification = createSpecification(criteria);
        return pipelineTagRepository.findAll(specification, page).map(pipelineTagMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PipelineTagCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PipelineTag> specification = createSpecification(criteria);
        return pipelineTagRepository.count(specification);
    }

    /**
     * Function to convert {@link PipelineTagCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PipelineTag> createSpecification(PipelineTagCriteria criteria) {
        Specification<PipelineTag> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PipelineTag_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), PipelineTag_.name));
            }
            if (criteria.getPipelineId() != null) {
                specification =
                    specification.and(
                        buildSpecification(
                            criteria.getPipelineId(),
                            root -> root.join(PipelineTag_.pipeline, JoinType.LEFT).get(Pipeline_.id)
                        )
                    );
            }
        }
        return specification;
    }
}
