package com.mgs.service;

import com.mgs.domain.ReportRun;
import com.mgs.repository.ReportRunRepository;
import com.mgs.service.dto.ReportRunDTO;
import com.mgs.service.mapper.ReportRunMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mgs.domain.ReportRun}.
 */
@Service
@Transactional
public class ReportRunService {

    private static final Logger LOG = LoggerFactory.getLogger(ReportRunService.class);

    private final ReportRunRepository reportRunRepository;

    private final ReportRunMapper reportRunMapper;

    public ReportRunService(ReportRunRepository reportRunRepository, ReportRunMapper reportRunMapper) {
        this.reportRunRepository = reportRunRepository;
        this.reportRunMapper = reportRunMapper;
    }

    /**
     * Save a reportRun.
     *
     * @param reportRunDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportRunDTO save(ReportRunDTO reportRunDTO) {
        LOG.debug("Request to save ReportRun : {}", reportRunDTO);
        ReportRun reportRun = reportRunMapper.toEntity(reportRunDTO);
        reportRun = reportRunRepository.save(reportRun);
        return reportRunMapper.toDto(reportRun);
    }

    /**
     * Update a reportRun.
     *
     * @param reportRunDTO the entity to save.
     * @return the persisted entity.
     */
    public ReportRunDTO update(ReportRunDTO reportRunDTO) {
        LOG.debug("Request to update ReportRun : {}", reportRunDTO);
        ReportRun reportRun = reportRunMapper.toEntity(reportRunDTO);
        reportRun = reportRunRepository.save(reportRun);
        return reportRunMapper.toDto(reportRun);
    }

    /**
     * Partially update a reportRun.
     *
     * @param reportRunDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReportRunDTO> partialUpdate(ReportRunDTO reportRunDTO) {
        LOG.debug("Request to partially update ReportRun : {}", reportRunDTO);

        return reportRunRepository
            .findById(reportRunDTO.getId())
            .map(existingReportRun -> {
                reportRunMapper.partialUpdate(existingReportRun, reportRunDTO);

                return existingReportRun;
            })
            .map(reportRunRepository::save)
            .map(reportRunMapper::toDto);
    }

    /**
     * Get one reportRun by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReportRunDTO> findOne(Long id) {
        LOG.debug("Request to get ReportRun : {}", id);
        return reportRunRepository.findById(id).map(reportRunMapper::toDto);
    }

    /**
     * Delete the reportRun by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ReportRun : {}", id);
        reportRunRepository.deleteById(id);
    }
}
