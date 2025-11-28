package com.mgs.service;

import com.mgs.domain.TenantBranding;
import com.mgs.repository.TenantBrandingRepository;
import com.mgs.service.dto.TenantBrandingDTO;
import com.mgs.service.mapper.TenantBrandingMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mgs.domain.TenantBranding}.
 */
@Service
@Transactional
public class TenantBrandingService {

    private static final Logger LOG = LoggerFactory.getLogger(TenantBrandingService.class);

    private final TenantBrandingRepository tenantBrandingRepository;

    private final TenantBrandingMapper tenantBrandingMapper;

    public TenantBrandingService(TenantBrandingRepository tenantBrandingRepository, TenantBrandingMapper tenantBrandingMapper) {
        this.tenantBrandingRepository = tenantBrandingRepository;
        this.tenantBrandingMapper = tenantBrandingMapper;
    }

    /**
     * Save a tenantBranding.
     *
     * @param tenantBrandingDTO the entity to save.
     * @return the persisted entity.
     */
    public TenantBrandingDTO save(TenantBrandingDTO tenantBrandingDTO) {
        LOG.debug("Request to save TenantBranding : {}", tenantBrandingDTO);
        TenantBranding tenantBranding = tenantBrandingMapper.toEntity(tenantBrandingDTO);
        tenantBranding = tenantBrandingRepository.save(tenantBranding);
        return tenantBrandingMapper.toDto(tenantBranding);
    }

    /**
     * Update a tenantBranding.
     *
     * @param tenantBrandingDTO the entity to save.
     * @return the persisted entity.
     */
    public TenantBrandingDTO update(TenantBrandingDTO tenantBrandingDTO) {
        LOG.debug("Request to update TenantBranding : {}", tenantBrandingDTO);
        TenantBranding tenantBranding = tenantBrandingMapper.toEntity(tenantBrandingDTO);
        tenantBranding = tenantBrandingRepository.save(tenantBranding);
        return tenantBrandingMapper.toDto(tenantBranding);
    }

    /**
     * Partially update a tenantBranding.
     *
     * @param tenantBrandingDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TenantBrandingDTO> partialUpdate(TenantBrandingDTO tenantBrandingDTO) {
        LOG.debug("Request to partially update TenantBranding : {}", tenantBrandingDTO);

        return tenantBrandingRepository
            .findById(tenantBrandingDTO.getId())
            .map(existingTenantBranding -> {
                tenantBrandingMapper.partialUpdate(existingTenantBranding, tenantBrandingDTO);

                return existingTenantBranding;
            })
            .map(tenantBrandingRepository::save)
            .map(tenantBrandingMapper::toDto);
    }

    /**
     * Get one tenantBranding by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TenantBrandingDTO> findOne(Long id) {
        LOG.debug("Request to get TenantBranding : {}", id);
        return tenantBrandingRepository.findById(id).map(tenantBrandingMapper::toDto);
    }

    /**
     * Delete the tenantBranding by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TenantBranding : {}", id);
        tenantBrandingRepository.deleteById(id);
    }
}
