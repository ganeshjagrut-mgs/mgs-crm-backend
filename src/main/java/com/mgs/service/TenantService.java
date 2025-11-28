package com.mgs.service;

import com.mgs.domain.Tenant;
import com.mgs.repository.TenantRepository;
import com.mgs.service.dto.TenantDTO;
import com.mgs.service.mapper.TenantMapper;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mgs.domain.Tenant}.
 */
@Service
@Transactional
public class TenantService {

    private static final Logger LOG = LoggerFactory.getLogger(TenantService.class);

    private final TenantRepository tenantRepository;

    private final TenantMapper tenantMapper;

    public TenantService(TenantRepository tenantRepository, TenantMapper tenantMapper) {
        this.tenantRepository = tenantRepository;
        this.tenantMapper = tenantMapper;
    }

    /**
     * Save a tenant.
     *
     * @param tenantDTO the entity to save.
     * @return the persisted entity.
     */
    public TenantDTO save(TenantDTO tenantDTO) {
        LOG.debug("Request to save Tenant : {}", tenantDTO);
        Tenant tenant = tenantMapper.toEntity(tenantDTO);
        tenant = tenantRepository.save(tenant);
        return tenantMapper.toDto(tenant);
    }

    /**
     * Update a tenant.
     *
     * @param tenantDTO the entity to save.
     * @return the persisted entity.
     */
    public TenantDTO update(TenantDTO tenantDTO) {
        LOG.debug("Request to update Tenant : {}", tenantDTO);
        Tenant tenant = tenantMapper.toEntity(tenantDTO);
        tenant = tenantRepository.save(tenant);
        return tenantMapper.toDto(tenant);
    }

    /**
     * Partially update a tenant.
     *
     * @param tenantDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TenantDTO> partialUpdate(TenantDTO tenantDTO) {
        LOG.debug("Request to partially update Tenant : {}", tenantDTO);

        return tenantRepository
            .findById(tenantDTO.getId())
            .map(existingTenant -> {
                tenantMapper.partialUpdate(existingTenant, tenantDTO);

                return existingTenant;
            })
            .map(tenantRepository::save)
            .map(tenantMapper::toDto);
    }

    /**
     *  Get all the tenants where TenantProfile is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TenantDTO> findAllWhereTenantProfileIsNull() {
        LOG.debug("Request to get all tenants where TenantProfile is null");
        return StreamSupport.stream(tenantRepository.findAll().spliterator(), false)
            .filter(tenant -> tenant.getTenantProfile() == null)
            .map(tenantMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     *  Get all the tenants where TenantBranding is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<TenantDTO> findAllWhereTenantBrandingIsNull() {
        LOG.debug("Request to get all tenants where TenantBranding is null");
        return StreamSupport.stream(tenantRepository.findAll().spliterator(), false)
            .filter(tenant -> tenant.getTenantBranding() == null)
            .map(tenantMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }

    /**
     * Get one tenant by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TenantDTO> findOne(Long id) {
        LOG.debug("Request to get Tenant : {}", id);
        return tenantRepository.findById(id).map(tenantMapper::toDto);
    }

    /**
     * Delete the tenant by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Tenant : {}", id);
        tenantRepository.deleteById(id);
    }
}
