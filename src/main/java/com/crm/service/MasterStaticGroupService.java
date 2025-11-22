package com.crm.service;

import com.crm.service.dto.MasterStaticGroupDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.crm.domain.MasterStaticGroup}.
 */
public interface MasterStaticGroupService {
    /**
     * Save a masterStaticGroup.
     *
     * @param masterStaticGroupDTO the entity to save.
     * @return the persisted entity.
     */
    MasterStaticGroupDTO save(MasterStaticGroupDTO masterStaticGroupDTO);

    /**
     * Updates a masterStaticGroup.
     *
     * @param masterStaticGroupDTO the entity to update.
     * @return the persisted entity.
     */
    MasterStaticGroupDTO update(MasterStaticGroupDTO masterStaticGroupDTO);

    /**
     * Partially updates a masterStaticGroup.
     *
     * @param masterStaticGroupDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<MasterStaticGroupDTO> partialUpdate(MasterStaticGroupDTO masterStaticGroupDTO);

    /**
     * Get all the masterStaticGroups.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<MasterStaticGroupDTO> findAll(Pageable pageable);

    /**
     * Get the "id" masterStaticGroup.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<MasterStaticGroupDTO> findOne(Long id);

    /**
     * Delete the "id" masterStaticGroup.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
