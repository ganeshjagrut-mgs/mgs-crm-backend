package com.crm.service;

import com.crm.service.dto.PipelineDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.crm.domain.Pipeline}.
 */
public interface PipelineService {
    /**
     * Save a pipeline.
     *
     * @param pipelineDTO the entity to save.
     * @return the persisted entity.
     */
    PipelineDTO save(PipelineDTO pipelineDTO);

    /**
     * Updates a pipeline.
     *
     * @param pipelineDTO the entity to update.
     * @return the persisted entity.
     */
    PipelineDTO update(PipelineDTO pipelineDTO);

    /**
     * Partially updates a pipeline.
     *
     * @param pipelineDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PipelineDTO> partialUpdate(PipelineDTO pipelineDTO);

    /**
     * Get all the pipelines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PipelineDTO> findAll(Pageable pageable);

    /**
     * Get the "id" pipeline.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PipelineDTO> findOne(Long id);

    /**
     * Delete the "id" pipeline.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
