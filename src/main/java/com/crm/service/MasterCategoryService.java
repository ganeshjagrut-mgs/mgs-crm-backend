package com.crm.service;

import com.crm.service.dto.MasterCategoryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.crm.domain.MasterCategory}.
 */
public interface MasterCategoryService {
    /**
     * Save a masterCategory.
     *
     * @param masterCategoryDTO the entity to save.
     * @return the persisted entity.
     */
    MasterCategoryDTO save(MasterCategoryDTO masterCategoryDTO);

    /**
     * Updates a masterCategory.
     *
     * @param masterCategoryDTO the entity to update.
     * @return the persisted entity.
     */
    MasterCategoryDTO update(MasterCategoryDTO masterCategoryDTO);

    /**
     * Partially updates a masterCategory.
     *
     * @param masterCategoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MasterCategoryDTO> partialUpdate(MasterCategoryDTO masterCategoryDTO);

    /**
     * Get all the masterCategories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MasterCategoryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" masterCategory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MasterCategoryDTO> findOne(Long id);

    /**
     * Delete the "id" masterCategory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
