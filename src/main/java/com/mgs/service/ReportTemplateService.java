package com.mgs.service;

import com.mgs.domain.ReportTemplate;
import com.mgs.repository.ReportTemplateRepository;
import com.mgs.service.dto.ReportTemplateDTO;
import com.mgs.service.mapper.ReportTemplateMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mgs.domain.ReportTemplate}.
 */
@Service
@Transactional
public class ReportTemplateService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportTemplateService.class);

    private final ReportTemplateRepository reportTemplateRepository;

    private final ReportTemplateMapper reportTemplateMapper;

    public ReportTemplateService(ReportTemplateRepository reportTemplateRepository, ReportTemplateMapper reportTemplateMapper) {
        this.reportTemplateRepository = reportTemplateRepository;
        this.reportTemplateMapper = reportTemplateMapper;
    }

    /**
     * Save a reportTemplate.
     *
     * @param reportTemplateDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportTemplateDTO save(ReportTemplateDTO reportTemplateDTO) {
        LOG.debug("Request to save ReportTemplate : {}", reportTemplateDTO);
        ReportTemplate reportTemplate = reportTemplateMapper.toEntity(reportTemplateDTO);
        reportTemplate = reportTemplateRepository.save(reportTemplate);
        return reportTemplateMapper.toDto(reportTemplate);
    }

    /**
     * Update a reportTemplate.
     *
     * @param reportTemplateDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportTemplateDTO update(ReportTemplateDTO reportTemplateDTO) {
        LOG.debug("Request to update ReportTemplate : {}", reportTemplateDTO);
        ReportTemplate reportTemplate = reportTemplateMapper.toEntity(reportTemplateDTO);
        reportTemplate = reportTemplateRepository.save(reportTemplate);
        return reportTemplateMapper.toDto(reportTemplate);
    }

    /**
     * Partially update a reportTemplate.
     *
     * @param reportTemplateDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReportTemplateDTO> partialUpdate(ReportTemplateDTO reportTemplateDTO) {
        LOG.debug("Request to partially update ReportTemplate : {}", reportTemplateDTO);

        return reportTemplateRepository
            .findById(reportTemplateDTO.getId())
            .map(existingReportTemplate -> {
                reportTemplateMapper.partialUpdate(existingReportTemplate, reportTemplateDTO);

                return existingReportTemplate;
            })
            .map(reportTemplateRepository::save)
            .map(reportTemplateMapper::toDto);
    }

    /**
     * Get one reportTemplate by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReportTemplateDTO> findOne(Long id) {
        LOG.debug("Request to get ReportTemplate : {}", id);
        return reportTemplateRepository.findById(id).map(reportTemplateMapper::toDto);
    }

    /**
     * Delete the reportTemplate by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReportTemplate : {}", id);
        reportTemplateRepository.deleteById(id);
    }
}
