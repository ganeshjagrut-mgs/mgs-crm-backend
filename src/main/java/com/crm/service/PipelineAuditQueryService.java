package com.crm.service;

import com.crm.domain.*; // for static metamodels
import com.crm.domain.PipelineAudit;
import com.crm.repository.PipelineAuditRepository;
import com.crm.service.criteria.PipelineAuditCriteria;
import com.crm.service.dto.PipelineAuditDTO;
import com.crm.service.mapper.PipelineAuditMapper;
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
 * Service for executing complex queries for {@link PipelineAudit} entities in the database.
 * The main input is a {@link PipelineAuditCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PipelineAuditDTO} or a {@link Page} of {@link PipelineAuditDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PipelineAuditQueryService extends QueryService<PipelineAudit> {

    private final Logger log = LoggerFactory.getLogger(PipelineAuditQueryService.class);

    private final PipelineAuditRepository pipelineAuditRepository;

    private final PipelineAuditMapper pipelineAuditMapper;

    public PipelineAuditQueryService(PipelineAuditRepository pipelineAuditRepository, PipelineAuditMapper pipelineAuditMapper) {
        this.pipelineAuditRepository = pipelineAuditRepository;
        this.pipelineAuditMapper = pipelineAuditMapper;
    }

    /**
     * Return a {@link List} of {@link PipelineAuditDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PipelineAuditDTO> findByCriteria(PipelineAuditCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PipelineAudit> specification = createSpecification(criteria);
        return pipelineAuditMapper.toDto(pipelineAuditRepository.findAll(specification));
    }

    /**
     * Return a {@link Page} of {@link PipelineAuditDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PipelineAuditDTO> findByCriteria(PipelineAuditCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PipelineAudit> specification = createSpecification(criteria);
        return pipelineAuditRepository.findAll(specification, page).map(pipelineAuditMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PipelineAuditCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PipelineAudit> specification = createSpecification(criteria);
        return pipelineAuditRepository.count(specification);
    }

    /**
     * Function to convert {@link PipelineAuditCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PipelineAudit> createSpecification(PipelineAuditCriteria criteria) {
        Specification<PipelineAudit> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PipelineAudit_.id));
            }
            if (criteria.getEventTimestamp() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEventTimestamp(), PipelineAudit_.eventTimestamp));
            }
            if (criteria.getAction() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAction(), PipelineAudit_.action));
            }
            if (criteria.getRowId() != null) {
                specification = specification.and(buildSpecification(criteria.getRowId(), PipelineAudit_.rowId));
            }
            if (criteria.getCorrelationId() != null) {
                specification = specification.and(buildSpecification(criteria.getCorrelationId(), PipelineAudit_.correlationId));
            }
        }
        return specification;
    }
}
