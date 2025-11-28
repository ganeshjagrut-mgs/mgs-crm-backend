package com.mgs.service;

import com.mgs.domain.*; // for static metamodels
import com.mgs.domain.ReportRun;
import com.mgs.repository.ReportRunRepository;
import com.mgs.service.criteria.ReportRunCriteria;
import com.mgs.service.dto.ReportRunDTO;
import com.mgs.service.mapper.ReportRunMapper;
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
 * Service for executing complex queries for {@link ReportRun} entities in the database.
 * The main input is a {@link ReportRunCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ReportRunDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReportRunQueryService extends QueryService<ReportRun> {

    private static final Logger LOG = LoggerFactory.getLogger(ReportRunQueryService.class);

    private final ReportRunRepository reportRunRepository;

    private final ReportRunMapper reportRunMapper;

    public ReportRunQueryService(ReportRunRepository reportRunRepository, ReportRunMapper reportRunMapper) {
        this.reportRunRepository = reportRunRepository;
        this.reportRunMapper = reportRunMapper;
    }

    /**
     * Return a {@link Page} of {@link ReportRunDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReportRunDTO> findByCriteria(ReportRunCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReportRun> specification = createSpecification(criteria);
        return reportRunRepository.findAll(specification, page).map(reportRunMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReportRunCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ReportRun> specification = createSpecification(criteria);
        return reportRunRepository.count(specification);
    }

    /**
     * Function to convert {@link ReportRunCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ReportRun> createSpecification(ReportRunCriteria criteria) {
        Specification<ReportRun> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ReportRun_.id),
                buildStringSpecification(criteria.getName(), ReportRun_.name),
                buildStringSpecification(criteria.getFilterJson(), ReportRun_.filterJson),
                buildStringSpecification(criteria.getFormat(), ReportRun_.format),
                buildStringSpecification(criteria.getFilePath(), ReportRun_.filePath),
                buildSpecification(criteria.getTenantId(), root -> root.join(ReportRun_.tenant, JoinType.LEFT).get(Tenant_.id)),
                buildSpecification(criteria.getTemplateId(), root -> root.join(ReportRun_.template, JoinType.LEFT).get(ReportTemplate_.id)),
                buildSpecification(criteria.getGeneratedByUserId(), root ->
                    root.join(ReportRun_.generatedByUser, JoinType.LEFT).get(User_.id)
                )
            );
        }
        return specification;
    }
}
