package com.crm.service;

import com.crm.service.dto.SubPipelineOpenStageDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.crm.domain.SubPipelineOpenStage}.
 */
public interface SubPipelineOpenStageService {
    /**
     * Save a subPipelineOpenStage.
     *
     * @param subPipelineOpenStageDTO the entity to save.
     * @return the persisted entity.
     */
    SubPipelineOpenStageDTO save(SubPipelineOpenStageDTO subPipelineOpenStageDTO);

    /**
     * Updates a subPipelineOpenStage.
     *
     * @param subPipelineOpenStageDTO the entity to update.
     * @return the persisted entity.
     */
    SubPipelineOpenStageDTO update(SubPipelineOpenStageDTO subPipelineOpenStageDTO);

    /**
     * Partially updates a subPipelineOpenStage.
     *
     * @param subPipelineOpenStageDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SubPipelineOpenStageDTO> partialUpdate(SubPipelineOpenStageDTO subPipelineOpenStageDTO);

    /**
     * Get all the subPipelineOpenStages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SubPipelineOpenStageDTO> findAll(Pageable pageable);

    /**
     * Get the "id" subPipelineOpenStage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubPipelineOpenStageDTO> findOne(Long id);

    /**
     * Delete the "id" subPipelineOpenStage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
