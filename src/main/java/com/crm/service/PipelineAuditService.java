package com.crm.service;

import com.crm.service.dto.PipelineAuditDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.crm.domain.PipelineAudit}.
 */
public interface PipelineAuditService {
    /**
     * Save a pipelineAudit.
     *
     * @param pipelineAuditDTO the entity to save.
     * @return the persisted entity.
     */
    PipelineAuditDTO save(PipelineAuditDTO pipelineAuditDTO);

    /**
     * Updates a pipelineAudit.
     *
     * @param pipelineAuditDTO the entity to update.
     * @return the persisted entity.
     */
    PipelineAuditDTO update(PipelineAuditDTO pipelineAuditDTO);

    /**
     * Partially updates a pipelineAudit.
     *
     * @param pipelineAuditDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PipelineAuditDTO> partialUpdate(PipelineAuditDTO pipelineAuditDTO);

    /**
     * Get all the pipelineAudits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PipelineAuditDTO> findAll(Pageable pageable);

    /**
     * Get the "id" pipelineAudit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PipelineAuditDTO> findOne(Long id);

    /**
     * Delete the "id" pipelineAudit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
