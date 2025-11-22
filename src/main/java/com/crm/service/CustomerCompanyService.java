package com.crm.service;

import com.crm.service.dto.CustomerCompanyDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.crm.domain.CustomerCompany}.
 */
public interface CustomerCompanyService {
    /**
     * Save a customerCompany.
     *
     * @param customerCompanyDTO the entity to save.
     * @return the persisted entity.
     */
    CustomerCompanyDTO save(CustomerCompanyDTO customerCompanyDTO);

    /**
     * Updates a customerCompany.
     *
     * @param customerCompanyDTO the entity to update.
     * @return the persisted entity.
     */
    CustomerCompanyDTO update(CustomerCompanyDTO customerCompanyDTO);

    /**
     * Partially updates a customerCompany.
     *
     * @param customerCompanyDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<CustomerCompanyDTO> partialUpdate(CustomerCompanyDTO customerCompanyDTO);

    /**
     * Get all the customerCompanies.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<CustomerCompanyDTO> findAll(Pageable pageable);

    /**
     * Get the "id" customerCompany.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<CustomerCompanyDTO> findOne(Long id);

    /**
     * Delete the "id" customerCompany.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
