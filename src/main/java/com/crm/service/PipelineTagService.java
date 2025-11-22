package com.crm.service;

import com.crm.service.dto.PipelineTagDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.crm.domain.PipelineTag}.
 */
public interface PipelineTagService {
    /**
     * Save a pipelineTag.
     *
     * @param pipelineTagDTO the entity to save.
     * @return the persisted entity.
     */
    PipelineTagDTO save(PipelineTagDTO pipelineTagDTO);

    /**
     * Updates a pipelineTag.
     *
     * @param pipelineTagDTO the entity to update.
     * @return the persisted entity.
     */
    PipelineTagDTO update(PipelineTagDTO pipelineTagDTO);

    /**
     * Partially updates a pipelineTag.
     *
     * @param pipelineTagDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<PipelineTagDTO> partialUpdate(PipelineTagDTO pipelineTagDTO);

    /**
     * Get all the pipelineTags.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PipelineTagDTO> findAll(Pageable pageable);

    /**
     * Get the "id" pipelineTag.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PipelineTagDTO> findOne(Long id);

    /**
     * Delete the "id" pipelineTag.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
