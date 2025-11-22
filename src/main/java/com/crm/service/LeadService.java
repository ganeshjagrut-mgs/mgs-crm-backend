package com.crm.service;

import com.crm.service.dto.LeadDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.crm.domain.Lead}.
 */
public interface LeadService {
    /**
     * Save a lead.
     *
     * @param leadDTO the entity to save.
     * @return the persisted entity.
     */
    LeadDTO save(LeadDTO leadDTO);

    /**
     * Updates a lead.
     *
     * @param leadDTO the entity to update.
     * @return the persisted entity.
     */
    LeadDTO update(LeadDTO leadDTO);

    /**
     * Partially updates a lead.
     *
     * @param leadDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<LeadDTO> partialUpdate(LeadDTO leadDTO);

    /**
     * Get all the leads.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<LeadDTO> findAll(Pageable pageable);

    /**
     * Get the "id" lead.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<LeadDTO> findOne(Long id);

    /**
     * Delete the "id" lead.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
