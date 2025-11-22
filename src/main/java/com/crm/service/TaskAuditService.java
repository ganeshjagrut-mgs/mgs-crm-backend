package com.crm.service;

import com.crm.service.dto.TaskAuditDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.crm.domain.TaskAudit}.
 */
public interface TaskAuditService {
    /**
     * Save a taskAudit.
     *
     * @param taskAuditDTO the entity to save.
     * @return the persisted entity.
     */
    TaskAuditDTO save(TaskAuditDTO taskAuditDTO);

    /**
     * Updates a taskAudit.
     *
     * @param taskAuditDTO the entity to update.
     * @return the persisted entity.
     */
    TaskAuditDTO update(TaskAuditDTO taskAuditDTO);

    /**
     * Partially updates a taskAudit.
     *
     * @param taskAuditDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TaskAuditDTO> partialUpdate(TaskAuditDTO taskAuditDTO);

    /**
     * Get all the taskAudits.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TaskAuditDTO> findAll(Pageable pageable);

    /**
     * Get the "id" taskAudit.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TaskAuditDTO> findOne(Long id);

    /**
     * Delete the "id" taskAudit.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
