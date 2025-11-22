package com.crm.service;

import com.crm.service.dto.SubPipelineCloseStageDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.crm.domain.SubPipelineCloseStage}.
 */
public interface SubPipelineCloseStageService {
    /**
     * Save a subPipelineCloseStage.
     *
     * @param subPipelineCloseStageDTO the entity to save.
     * @return the persisted entity.
     */
    SubPipelineCloseStageDTO save(SubPipelineCloseStageDTO subPipelineCloseStageDTO);

    /**
     * Updates a subPipelineCloseStage.
     *
     * @param subPipelineCloseStageDTO the entity to update.
     * @return the persisted entity.
     */
    SubPipelineCloseStageDTO update(SubPipelineCloseStageDTO subPipelineCloseStageDTO);

    /**
     * Partially updates a subPipelineCloseStage.
     *
     * @param subPipelineCloseStageDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<SubPipelineCloseStageDTO> partialUpdate(SubPipelineCloseStageDTO subPipelineCloseStageDTO);

    /**
     * Get all the subPipelineCloseStages.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<SubPipelineCloseStageDTO> findAll(Pageable pageable);

    /**
     * Get the "id" subPipelineCloseStage.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<SubPipelineCloseStageDTO> findOne(Long id);

    /**
     * Delete the "id" subPipelineCloseStage.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
