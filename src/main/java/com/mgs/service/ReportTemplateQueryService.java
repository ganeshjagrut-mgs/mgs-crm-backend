package com.mgs.service;

import com.mgs.domain.*; // for static metamodels
import com.mgs.domain.ReportTemplate;
import com.mgs.repository.ReportTemplateRepository;
import com.mgs.service.criteria.ReportTemplateCriteria;
import com.mgs.service.dto.ReportTemplateDTO;
import com.mgs.service.mapper.ReportTemplateMapper;
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
 * Service for executing complex queries for {@link ReportTemplate} entities in the database.
 * The main input is a {@link ReportTemplateCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ReportTemplateDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReportTemplateQueryService extends QueryService<ReportTemplate> {

    private static final Logger LOG = LoggerFactory.getLogger(ReportTemplateQueryService.class);

    private final ReportTemplateRepository reportTemplateRepository;

    private final ReportTemplateMapper reportTemplateMapper;

    public ReportTemplateQueryService(ReportTemplateRepository reportTemplateRepository, ReportTemplateMapper reportTemplateMapper) {
        this.reportTemplateRepository = reportTemplateRepository;
        this.reportTemplateMapper = reportTemplateMapper;
    }

    /**
     * Return a {@link Page} of {@link ReportTemplateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReportTemplateDTO> findByCriteria(ReportTemplateCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ReportTemplate> specification = createSpecification(criteria);
        return reportTemplateRepository.findAll(specification, page).map(reportTemplateMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReportTemplateCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ReportTemplate> specification = createSpecification(criteria);
        return reportTemplateRepository.count(specification);
    }

    /**
     * Function to convert {@link ReportTemplateCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ReportTemplate> createSpecification(ReportTemplateCriteria criteria) {
        Specification<ReportTemplate> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ReportTemplate_.id),
                buildStringSpecification(criteria.getCode(), ReportTemplate_.code),
                buildStringSpecification(criteria.getName(), ReportTemplate_.name),
                buildStringSpecification(criteria.getDescription(), ReportTemplate_.description),
                buildStringSpecification(criteria.getConfigJson(), ReportTemplate_.configJson),
                buildSpecification(criteria.getIsActive(), ReportTemplate_.isActive),
                buildSpecification(criteria.getTenantId(), root -> root.join(ReportTemplate_.tenant, JoinType.LEFT).get(Tenant_.id))
            );
        }
        return specification;
    }
}
