package com.crm.service;

import com.crm.service.dto.EncryptionDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.crm.domain.Encryption}.
 */
public interface EncryptionService {
    /**
     * Save a encryption.
     *
     * @param encryptionDTO the entity to save.
     * @return the persisted entity.
     */
    EncryptionDTO save(EncryptionDTO encryptionDTO);

    /**
     * Updates a encryption.
     *
     * @param encryptionDTO the entity to update.
     * @return the persisted entity.
     */
    EncryptionDTO update(EncryptionDTO encryptionDTO);

    /**
     * Partially updates a encryption.
     *
     * @param encryptionDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<EncryptionDTO> partialUpdate(EncryptionDTO encryptionDTO);

    /**
     * Get all the encryptions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<EncryptionDTO> findAll(Pageable pageable);

    /**
     * Get the "id" encryption.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<EncryptionDTO> findOne(Long id);

    /**
     * Delete the "id" encryption.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
