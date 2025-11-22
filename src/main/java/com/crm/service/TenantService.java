package com.crm.service;

import com.crm.service.dto.TenantDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link com.crm.domain.Tenant}.
 */
public interface TenantService {
    /**
     * Save a tenant.
     *
     * @param tenantDTO the entity to save.
     * @return the persisted entity.
     */
    TenantDTO save(TenantDTO tenantDTO);

    /**
     * Updates a tenant.
     *
     * @param tenantDTO the entity to update.
     * @return the persisted entity.
     */
    TenantDTO update(TenantDTO tenantDTO);

    /**
     * Partially updates a tenant.
     *
     * @param tenantDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<TenantDTO> partialUpdate(TenantDTO tenantDTO);

    /**
     * Get all the tenants.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TenantDTO> findAll(Pageable pageable);

    /**
     * Get all the TenantDTO where Encryption is {@code null}.
     *
     * @return the {@link List} of entities.
     */
    List<TenantDTO> findAllWhereEncryptionIsNull();

    /**
     * Get all the tenants with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<TenantDTO> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" tenant.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<TenantDTO> findOne(Long id);

    /**
     * Delete the "id" tenant.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Register a new tenant.
     *
     * @param tenantRegistrationDTO the entity to register.
     * @return the persisted tenant.
     */
    TenantDTO registerTenant(com.crm.service.dto.TenantRegistrationDTO tenantRegistrationDTO);
}
