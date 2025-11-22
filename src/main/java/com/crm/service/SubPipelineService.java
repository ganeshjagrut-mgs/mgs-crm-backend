package com.crm.service;

import com.crm.service.dto.SubPipelineDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.crm.domain.SubPipeline}.
 */
public interface SubPipelineService {
    /**
     * Save a subPipeline.
     *
     * @param subPipelineDTO the entity to save.
     * @return the persisted entity.
     */
    SubPipelineDTO save(SubPipelineDTO subPipelineDTO);

    /**
     * Updates a subPipeline.
     *
     * @param subPipelineDTO the entity to update.
     * @return the persisted entity.
     */
    SubPipelineDTO update(SubPipelineDTO subPipelineDTO);

    /**
     * Partially updates a subPipeline.
     *
     * @param subPipelineDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SubPipelineDTO> partialUpdate(SubPipelineDTO subPipelineDTO);

    /**
     * Get all the subPipelines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SubPipelineDTO> findAll(Pageable pageable);

    /**
     * Get the "id" subPipeline.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubPipelineDTO> findOne(Long id);

    /**
     * Delete the "id" subPipeline.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
