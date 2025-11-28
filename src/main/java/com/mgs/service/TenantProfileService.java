package com.mgs.service;

import com.mgs.domain.TenantProfile;
import com.mgs.repository.TenantProfileRepository;
import com.mgs.service.dto.TenantProfileDTO;
import com.mgs.service.mapper.TenantProfileMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mgs.domain.TenantProfile}.
 */
@Service
@Transactional
public class TenantProfileService {

    private static final Logger LOG = LoggerFactory.getLogger(TenantProfileService.class);

    private final TenantProfileRepository tenantProfileRepository;

    private final TenantProfileMapper tenantProfileMapper;

    public TenantProfileService(TenantProfileRepository tenantProfileRepository, TenantProfileMapper tenantProfileMapper) {
        this.tenantProfileRepository = tenantProfileRepository;
        this.tenantProfileMapper = tenantProfileMapper;
    }

    /**
     * Save a tenantProfile.
     *
     * @param tenantProfileDTO the entity to save.
     * @return the persisted entity.
     */
    public TenantProfileDTO save(TenantProfileDTO tenantProfileDTO) {
        LOG.debug("Request to save TenantProfile : {}", tenantProfileDTO);
        TenantProfile tenantProfile = tenantProfileMapper.toEntity(tenantProfileDTO);
        tenantProfile = tenantProfileRepository.save(tenantProfile);
        return tenantProfileMapper.toDto(tenantProfile);
    }

    /**
     * Update a tenantProfile.
     *
     * @param tenantProfileDTO the entity to save.
     * @return the persisted entity.
     */
    public TenantProfileDTO update(TenantProfileDTO tenantProfileDTO) {
        LOG.debug("Request to update TenantProfile : {}", tenantProfileDTO);
        TenantProfile tenantProfile = tenantProfileMapper.toEntity(tenantProfileDTO);
        tenantProfile = tenantProfileRepository.save(tenantProfile);
        return tenantProfileMapper.toDto(tenantProfile);
    }

    /**
     * Partially update a tenantProfile.
     *
     * @param tenantProfileDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<TenantProfileDTO> partialUpdate(TenantProfileDTO tenantProfileDTO) {
        LOG.debug("Request to partially update TenantProfile : {}", tenantProfileDTO);

        return tenantProfileRepository
            .findById(tenantProfileDTO.getId())
            .map(existingTenantProfile -> {
                tenantProfileMapper.partialUpdate(existingTenantProfile, tenantProfileDTO);

                return existingTenantProfile;
            })
            .map(tenantProfileRepository::save)
            .map(tenantProfileMapper::toDto);
    }

    /**
     * Get one tenantProfile by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<TenantProfileDTO> findOne(Long id) {
        LOG.debug("Request to get TenantProfile : {}", id);
        return tenantProfileRepository.findById(id).map(tenantProfileMapper::toDto);
    }

    /**
     * Delete the tenantProfile by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete TenantProfile : {}", id);
        tenantProfileRepository.deleteById(id);
    }
}
