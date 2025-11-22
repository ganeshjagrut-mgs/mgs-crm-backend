package com.crm.service;

import com.crm.service.dto.EntityTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.crm.domain.EntityType}.
 */
public interface EntityTypeService {
    /**
     * Save a entityType.
     *
     * @param entityTypeDTO the entity to save.
     * @return the persisted entity.
     */
    EntityTypeDTO save(EntityTypeDTO entityTypeDTO);

    /**
     * Updates a entityType.
     *
     * @param entityTypeDTO the entity to update.
     * @return the persisted entity.
     */
    EntityTypeDTO update(EntityTypeDTO entityTypeDTO);

    /**
     * Partially updates a entityType.
     *
     * @param entityTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EntityTypeDTO> partialUpdate(EntityTypeDTO entityTypeDTO);

    /**
     * Get all the entityTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EntityTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" entityType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EntityTypeDTO> findOne(Long id);

    /**
     * Delete the "id" entityType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
