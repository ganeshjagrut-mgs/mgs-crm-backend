package com.crm.service;

import com.crm.service.dto.MasterStaticTypeDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.crm.domain.MasterStaticType}.
 */
public interface MasterStaticTypeService {
    /**
     * Save a masterStaticType.
     *
     * @param masterStaticTypeDTO the entity to save.
     * @return the persisted entity.
     */
    MasterStaticTypeDTO save(MasterStaticTypeDTO masterStaticTypeDTO);

    /**
     * Updates a masterStaticType.
     *
     * @param masterStaticTypeDTO the entity to update.
     * @return the persisted entity.
     */
    MasterStaticTypeDTO update(MasterStaticTypeDTO masterStaticTypeDTO);

    /**
     * Partially updates a masterStaticType.
     *
     * @param masterStaticTypeDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MasterStaticTypeDTO> partialUpdate(MasterStaticTypeDTO masterStaticTypeDTO);

    /**
     * Get all the masterStaticTypes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MasterStaticTypeDTO> findAll(Pageable pageable);

    /**
     * Get the "id" masterStaticType.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MasterStaticTypeDTO> findOne(Long id);

    /**
     * Delete the "id" masterStaticType.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
